package com.example.testing.tone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ThreeFragment extends Fragment  implements View.OnClickListener{


    private TextView activitynum;
    private TextView recordnum;
    private TextView longtime;
    private TextView success;
    private ListView listView;
    private MyHelper myHelper;
    private SQLiteDatabase db;
    Vector<Thing> thingList;

    private String _id;
    private String _time;
    private String _thing;
    private String _encourage;
    private String _hour;
    private String _minutes;
    private int succes_time = 0;
    private ContentValues values;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tab04 = inflater.inflate(R.layout.listview,container,false);
       getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        //如果表不存在则新建表
        db = getActivity().openOrCreateDatabase("information.db",Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS get_time(_id INTEGER PRIMARY KEY AUTOINCREMENT,three INTEGER,eleven INTEGER,fourteen INTEGER,twentyone INTEGER ,thirty INTEGER)");

        listView = (ListView) tab04.findViewById(R.id.list);
        activitynum = (TextView) tab04.findViewById(R.id.activitynum);
        recordnum = (TextView) tab04.findViewById(R.id.num);
        longtime = (TextView)tab04.findViewById(R.id.longtime);
        success = (TextView)tab04.findViewById(R.id.success);
        myHelper = new MyHelper(getActivity());
        thingList = new Vector<>();
        values = new ContentValues();

        db = myHelper.getWritableDatabase();
        values.put("three",0);
        values.put("eleven",0);
        values.put("fourteen",0);
        values.put("twentyone",0);
        values.put("thirty",0);
        db.insert("get_time",null,values);
        db.close();

        updateList();

        initsuccess();
        return tab04;
    }

    public void  initsuccess(){
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from get_time",null);
        cursor.moveToFirst();
        int _id = cursor.getInt(0);
        int three_time = cursor.getInt(1);
        if(succes_time >= 3)
            three_time ++;
        int eleven_time = cursor.getInt(2);
        if(succes_time >= 7)
            eleven_time++;
        int fourteen_time = cursor.getInt(3);
        if(succes_time >= 14)
            fourteen_time++;
        int twentyone_time = cursor.getInt(4);
        if(succes_time >= 21)
            twentyone_time++;
        int thirty_time = cursor.getInt(5);
        if(succes_time >= 30)
            thirty_time++;
        List<Type> list = new ArrayList<>();
        Data data;
        Type type = new Type("打卡成就" );
        data = new Data("三天打鱼", "打卡三次", "获得"+ String.valueOf(three_time) +"次");
        type.addItem(data);
        data = new Data("习惯养成", "打卡7次", "获得"+ String.valueOf(fourteen_time) +"次");
        type.addItem(data);
        data = new Data("渐入佳境", "打卡14次", "获得"+ String.valueOf(eleven_time) +"次");
        type.addItem(data);
        data = new Data("轻车熟路", "打卡21次", "获得"+ String.valueOf(twentyone_time) +"次");
        type.addItem(data);
        data = new Data("打卡大魔王", "打卡30次", "获得"+ String.valueOf(thirty_time) +"次");
        type.addItem(data);
        list.add(type);
        MyAdapterL adapter = new MyAdapterL(getActivity(), list);
        listView.setAdapter(adapter);

        db = myHelper.getWritableDatabase();
        db.execSQL("UPDATE get_time SET three = \"" + three_time + "\" , eleven = \"" + eleven_time + "\" , fourteen = \"" + fourteen_time + "\" , twentyone = \"" + twentyone_time +"\" , thirty = \"" + thirty_time + "\" WHERE _id = "+_id);
        db.close();
    }

    @Override
    public void onClick(View v) {

    }

    public void updateList(){
        succes_time = 0;
        thingList.clear();
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from information",null);
        while(cursor.moveToNext()){
            _id = cursor.getString(0);
            _thing = cursor.getString(1);
            _encourage = cursor.getString(2);
            _time = cursor.getString(3);
            int _flag = cursor.getInt(4);
            _hour = cursor.getString(5);
            int hour = Integer.parseInt(_hour);
            _minutes = cursor.getString(6);
            int minute = Integer.parseInt(_minutes);
            boolean a;
            if(_flag==1) {
                a=true;
                succes_time = succes_time + 1;
            }
            else a=false;
            Thing thing_data = new Thing(_id,_thing,_encourage,_time,a,_hour,_minutes);
            thingList.addElement(thing_data);

        }
        String num = String.valueOf(thingList.size());
        activitynum.setText(num);
        String success_num = String.valueOf(succes_time);
        recordnum.setText(success_num);
        longtime.setText(success_num);
        db.close();
    }
}
