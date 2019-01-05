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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


import static android.content.Context.ALARM_SERVICE;

public class OneFragment extends Fragment  implements View.OnClickListener{
    private float y1;//按下点的y坐标
    private float y2;//移动时的y坐标
    private int height;
    private MyShowAdapter myadapter;//ListView的数据适配器

    private DatePicker datePicker;
    private Button create;
    private Button create1;
    private Button history;
    private LinearLayout dateLiner;
    private RelativeLayout.LayoutParams linearParams;
    private SwipeMenuListView lv;//listView
    private MyHelper myHelper;//数据库
    Vector<Thing> thingList;

    private Calendar mCalendar;
    private List<String> id_list = new ArrayList<String>();

    private SQLiteDatabase db;
    private String _id;
    private String _thing;
    private String _encourage;
    private String _time;
    private String _hour;
    private String _minutes;

    private boolean isGetData = false;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tab04 = inflater.inflate(R.layout.activity_main,container,false);

        Home.MyTouchListener myTouchListener = new Home.MyTouchListener() {
            public boolean onTouchEvent(MotionEvent event){
                // 处理手势事件
                linearParams = (RelativeLayout.LayoutParams) dateLiner.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE: {
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        y2 = event.getY();
                        if(y1 - y2 >10){//向上滑日历缩小
                            linearParams.height=300;
                        }else if(y2 - y1 >10){//向下滑日历变大
                            linearParams.height=height;
                        }
                        dateLiner.setLayoutParams(linearParams);
                        break;
                }
                return getActivity().onTouchEvent(event);
            }
        };
        ((Home)this.getActivity()).registerMyTouchListener(myTouchListener);


        datePicker = (DatePicker) tab04.findViewById(R.id.datePicker);
        create = (Button) tab04.findViewById(R.id.create);
        create.setOnClickListener(this);
        create1 = (Button) tab04.findViewById(R.id.create1);
        create1.setOnClickListener(this);
        history = (Button) tab04.findViewById(R.id.history);
        history.setOnClickListener(this);
        dateLiner = (LinearLayout)tab04.findViewById(R.id.dateLiner);
        datePicker.setCalendarViewShown(true);
        datePicker.setSpinnersShown(false);
        height = dateLiner.getLayoutParams().height;

        thingList = new Vector<>();
        lv = (SwipeMenuListView) tab04.findViewById(R.id.lv);
        myHelper = new MyHelper(getActivity());


        //如果表不存在则新建表
        db = getActivity().openOrCreateDatabase("information.db",Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS information(_id INTEGER PRIMARY KEY AUTOINCREMENT,thing VARCHAR(20),encourage VARCHAR(20),time VARCHAR(20),flag INTEGER ,hour INTEGER , minutes INTEGER)");

        updateList();

        myadapter = new MyShowAdapter(getActivity(), thingList);
        lv.setAdapter(myadapter);

        initListView();
        return tab04;
    }


    @Override
    public void onResume() {
        if (!isGetData) {
            //   这里可以做网络请求或者需要的数据刷新操作
            updateList();
            myadapter.notifyDataSetChanged();
            isGetData = true;
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    public void initListView(){
        //点击ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                db = myHelper.getWritableDatabase();
                Thing thing = thingList.get(position);
                String _id = thingList.get(position).get_id().toString();
                thingList.remove(position);
                boolean _flag = thing.get_flag();
                _flag = !_flag;
                if (_flag){
                    db.execSQL("UPDATE information SET flag = 1 WHERE _id = " + _id);
                    int x =Integer.parseInt(_id);
                    stopRemind(x);//删除闹钟
                }
                else{
                    db.execSQL("UPDATE information SET flag = 0 WHERE _id = " + _id);
                }
                thing.set_flag(_flag);
                thingList.add(position, thing);
                myadapter.notifyDataSetChanged();
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

                //同理create删除item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(180);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
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
                    case 1:
                        //删除的逻辑
                        String condition = thingList.get(position).get_id().toString();
                        db = myHelper.getWritableDatabase();
                        db.execSQL("DELETE FROM information WHERE _id = " + condition);
                        db.close();
                        thingList.remove(position);
                        myadapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        if (thingList.size() <= 0) {//如果没数据则显示"新建一个任务"按钮
                            create1.setVisibility(create1.VISIBLE);
                        }

                        int x =Integer.parseInt(condition);
                        stopRemind(x);//删除闹钟
                        break;
                }
                return false;
            }
        });
    }


    //更新列表
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


    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.create:
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
    

    private void startRemind(int hour ,int minute){
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
//           manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
        }
    }

    // 关闭提醒
    private void stopRemind(int x){

        Intent intent = new Intent(getActivity(), MyReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), x,
                intent, 0);
        AlarmManager am = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
        //Toast.makeText(this, "关闭了提醒", Toast.LENGTH_SHORT).show();

    }
    //数据回传
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
                updateList();
        myadapter.notifyDataSetChanged();
    }
}
