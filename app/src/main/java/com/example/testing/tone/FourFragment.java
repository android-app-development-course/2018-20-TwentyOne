package com.example.testing.tone;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FourFragment extends Fragment  implements View.OnClickListener{

    TextView ac;
    EditText ni;
    EditText na;
    EditText sex;
    EditText address;
    EditText saying;
    Button change;
    String account;
    MySqlHelper mysqlhelper;
    SQLiteDatabase db;
    String test="hea";
    String fl="fl";


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View tab04 = inflater.inflate(R.layout.me,container,false);

        ni=tab04.findViewById(R.id.editText_ni);
        na=tab04.findViewById(R.id.editText_name);
        sex=tab04.findViewById(R.id.editText_sex);
        address=tab04.findViewById(R.id.editText_address);
        saying=tab04.findViewById(R.id.editText_saying);
        ac=tab04.findViewById(R.id.textView_tietle);

        change=tab04.findViewById(R.id.button_change);
        change.setOnClickListener(this);

        mysqlhelper=new MySqlHelper(getActivity(), "information.db", null, 1);
        db= getActivity().openOrCreateDatabase("information.db", Context.MODE_PRIVATE, null);

        db = mysqlhelper.getReadableDatabase();
        Cursor cursor0 = db.query("flag", new String[]{"name","fl"}, null, null, null, null, null);
        while (cursor0.moveToNext()) {
            if (fl.equals(cursor0.getString(cursor0.getColumnIndex("fl")))) {//如果在数据库中找到了该账号，则允许修改
               test=cursor0.getString(cursor0.getColumnIndex("name"));
                Toast.makeText(getActivity(),test, Toast.LENGTH_SHORT).show();
                break;
            }
        }

        /*创建表，并判断是否已经存在此表，没创建，则创建并初始化*/
        if (! mysqlhelper.tabIsExist("account")) {
            db.execSQL("CREATE TABLE account (_id integer primary key autoincrement, ni varchar(20),name varchar(20),sex varchar(20),address varchar(50),saying varchar(80))");
        }else {
            db = mysqlhelper.getReadableDatabase();
            Cursor cursor = db.query("account", new String[]{"ni", "name","sex","address","saying"}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                if (test.equals(cursor.getString(cursor.getColumnIndex("name")))) {//如果在数据库中找到了该账号，则允许修改
                    ni.setText(cursor.getString(cursor.getColumnIndex("ni")));
                    na.setText(test);
                    sex.setText(cursor.getString(cursor.getColumnIndex("sex")));
                    address.setText(cursor.getString(cursor.getColumnIndex("address")));
                    saying.setText(cursor.getString(cursor.getColumnIndex("saying")));
                    break;
                }
            }
            cursor.close();
        }




        return tab04;
    }

    @Override
    public void onClick(View v) {
        String ni1;
        String sex1;
        String na1;
        String address1;
        String saying1;
        ContentValues values1;
        ContentValues values2;
        SQLiteDatabase db1;
        boolean creatuser=false;

       if(v.getId()==R.id.button_change){
           ni1=ni.getText().toString();
           na1=na.getText().toString();
           sex1=sex.getText().toString();
           address1=address.getText().toString();
           saying1=saying.getText().toString();
           db1 = mysqlhelper.getReadableDatabase();
           db=mysqlhelper.getWritableDatabase();
           Cursor cursor = db1.query("user", new String[]{"name"}, null, null, null, null, null);
           while (cursor.moveToNext()) {
               if (na1.equals(cursor.getString(cursor.getColumnIndex("name")))) {//如果在数据库中找到了该账号，则允许修改
                   creatuser = true;//不允许创建
                   break;
               }
           }
           cursor.close();
           if(creatuser==true){
               Cursor cursor1 = db1.query("account", new String[]{"name"}, null, null, null, null, null);
               while (cursor1.moveToNext()) {
                   if (na1.equals(cursor1.getString(cursor.getColumnIndex("name")))) {//如果在数据库中找到了该账号，则允许修改
                       values1=new ContentValues();
                       values1.put("ni",ni1);
                       values1.put("sex",sex1);
                       values1.put("address",address1);
                       values1.put("saying",saying1);

                       int num=db1.update("account",values1,"name=?",new String[]{na1});
                       Toast.makeText(getActivity(),"修改成功", Toast.LENGTH_SHORT).show();
                       cursor1.close();
                       break;
                   }
               }
               values2=new ContentValues();
               values2.put("ni",ni1);
               values2.put("name",na1);
               values2.put("sex",sex1);
               values2.put("address",address1);
               values2.put("saying",saying1);
               db.insert("account",null,values2);
               Toast.makeText(getActivity(),"修改成功", Toast.LENGTH_SHORT).show();
               db.close();
           }
           else{
               Toast.makeText(getActivity(),"输入账号错误，不得修改", Toast.LENGTH_SHORT).show();
           }


       }

    }
}
