package com.example.testing.tone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

public class TwoFragment extends Fragment implements View.OnClickListener {

    private Button create;
    private Button create1;
    private Button history;
    private String _id;
    private String _thing;
    private String _encourage;
    private String _time;
    private String _hour;
    private String _minutes;

    private SwipeMenuListView lv;//listView
    private MyHelper myHelper;//数据库
    Vector<Thing> thingList;
    private MyShowAdapter myadapter;//ListView的数据适配器
    private Calendar mCalendar;
    private List<String> id_list = new ArrayList<String>();
    private SQLiteDatabase db;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tab04 = inflater.inflate(R.layout.activity_detail,container,false);

        create = (Button) tab04.findViewById(R.id.create);
        create.setOnClickListener(this);
        create1 = (Button) tab04.findViewById(R.id.create1);
        create1.setOnClickListener(this);
        history = (Button) tab04.findViewById(R.id.history);
        history.setOnClickListener(this);
        lv = (SwipeMenuListView) tab04.findViewById(R.id.lv);

        thingList = new Vector<>();
        myHelper = new MyHelper(getActivity());


        updateList();

        myadapter = new MyShowAdapter(getActivity(), thingList);
        lv.setAdapter(myadapter);

        initListView();
        return tab04;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.create:
                intent = new Intent(getActivity(),ADD.class);
                startActivity(intent);

                break;
            case R.id.create1:
                intent = new Intent(getActivity(),ADD.class);
                startActivity(intent);

                break;
            case R.id.history:
                intent = new Intent(getActivity(),History.class);
                startActivity(intent);
                break;
        }
    }

    public void updateList(){
        thingList.clear();
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from information",null);
        while(cursor.moveToNext()){
            _id = cursor.getString(0);
            if(!id_list.contains(_id)){
                id_list.add(_id);
            }
            _thing = cursor.getString(1);
            _encourage = cursor.getString(2);
            _time = cursor.getString(3);
            int _flag = cursor.getInt(4);
            _hour = cursor.getString(5);
            int hour = Integer.parseInt(_hour);
            _minutes = cursor.getString(6);
            int minute = Integer.parseInt(_minutes);
            boolean a;
            if(_flag==1) a=true;
            else a=false;
            Thing thing_data = new Thing(_id,_thing,_encourage,_time,a,_hour,_minutes);
            thingList.addElement(thing_data);

            startRemind(hour ,minute);
        }
        db.close();
        if(thingList.size()>0){//如果有数据则隐藏"新建一个任务"按钮
            create1.setVisibility(create1.INVISIBLE);
        }else create1.setVisibility(create1.VISIBLE);
    }

    void initListView(){
        //点击ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _id = thingList.get(position).get_id().toString();
                Intent intent = new Intent(getActivity(),Edit.class);
                intent.putExtra("_id",_id);//传id
                startActivityForResult(intent,2);
            }
        });

        //长按
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "longClick" + thingList.get(position), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //加入侧滑显示的菜单
        //首先实例化SwipeMenuCreator对象
        SwipeMenuCreator creater = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create置顶item
                SwipeMenuItem item1 = new SwipeMenuItem(getActivity());
                // set item background
                item1.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                item1.setWidth(180);
                // set item title
                item1.setTitle("置顶");
                // set item title fontsize
                item1.setTitleSize(18);
                // set item title font color
                item1.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(item1);
            }
        };
        // set creator
        lv.setMenuCreator(creater);


        //菜单点击事件
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //置顶的逻辑
                        if (position == 0) {
                            Toast.makeText(getActivity(), "此项已经置顶", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        Thing str = thingList.get(position);
                        for (int i = position; i > 0; i--) {
                            Thing s = thingList.get(i - 1);
                            thingList.remove(i);
                            thingList.add(i, s);
                        }
                        thingList.remove(0);
                        thingList.add(0, str);
                        myadapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }
    private void startRemind(int hour ,int minute){

        Log.i("闹钟：","！！");
        if(id_list.contains(_id)){
            //得到日历实例，主要是为了下面的获取时间
            mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(System.currentTimeMillis());

            //获取当前毫秒值
            long systemTime = System.currentTimeMillis();
            // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
            mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            mCalendar.set(Calendar.HOUR_OF_DAY, hour);
            mCalendar.set(Calendar.MINUTE, minute);
            mCalendar.set(Calendar.SECOND,0);
            mCalendar.set(Calendar.MILLISECOND,0);

            long selectTime = mCalendar.getTimeInMillis();
            // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
            if(systemTime > selectTime) {
                mCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            //AlarmReceiver.class为广播接受者
            Intent intent = new Intent(getActivity(), MyReceiver.class);
            intent.putExtra("thing",_thing);
            int x = Integer.parseInt(_id);
            intent.putExtra("id",x);
            PendingIntent pi = PendingIntent.getBroadcast(getActivity(), x , intent, 0);//x不同闹钟才不会覆盖，表示不同闹钟
            //得到AlarmManager实例
            AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,  mCalendar.getTimeInMillis(), pi);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact(AlarmManager.RTC_WAKEUP,  mCalendar.getTimeInMillis(), pi);
            } else {
                manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,  mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
            }
        }
    }

    //数据回传
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        updateList();
        myadapter.notifyDataSetChanged();
    }
}
