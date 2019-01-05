package com.example.testing.tone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.Vector;

public class History extends Activity implements View.OnClickListener{

    private Button back;
    private SwipeMenuListView lv;//listView
    private MyHelper myHelper;//数据库
    private RelativeLayout bg;
    Vector<Thing> thingList;
    private MyShowAdapter myadapter;//ListView的数据适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        bg = (RelativeLayout) findViewById(R.id.bg);
        lv = (SwipeMenuListView) findViewById(R.id.lv);
        thingList = new Vector<>();
        myHelper = new MyHelper(this);

        updateList();
        if(thingList.size()>0){//如果有数据则隐藏历史界面
            bg.setVisibility(bg.INVISIBLE);
        }
        myadapter = new MyShowAdapter(this, thingList);
        lv.setAdapter(myadapter);

        initListView();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    //更新列表
    public void updateList(){
        thingList.clear();
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from information",null);
        while(cursor.moveToNext()){
            String _id = cursor.getString(0);
            String _thing = cursor.getString(1);
            String _encourage = cursor.getString(2);
            String _time = cursor.getString(3);
            int _flag = cursor.getInt(4);
            String _hour = cursor.getString(5);
            String _minutes = cursor.getString(6);
            if(_flag==1){
                boolean a=true;
                Thing thing_data = new Thing(_id,_thing,_encourage,_time,a,_hour,_minutes);
                thingList.addElement(thing_data);
            }
        }
        db.close();
    }

    //初始化ListView
    public void initListView(){
        //加入侧滑显示的菜单
        //首先实例化SwipeMenuCreator对象
        SwipeMenuCreator creater = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create置顶item
                SwipeMenuItem item1 = new SwipeMenuItem(History.this);
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
                            Toast.makeText(History.this, "此项已经置顶", Toast.LENGTH_SHORT).show();
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
}
