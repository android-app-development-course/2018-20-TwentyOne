package com.example.testing.tone;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity implements View.OnClickListener {

    MySqlHelper mysqlhelper;
    EditText UserName;
    EditText Key;
    EditText CKey;
    Button Register;
    SQLiteDatabase db;
    String mm="mm";
    String flag="fl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mysqlhelper=new MySqlHelper(this, "information.db", null, 1);
        db= this.openOrCreateDatabase("information.db", Context.MODE_PRIVATE, null);
        if (! mysqlhelper.tabIsExist("flag")) {
            db.execSQL("CREATE TABLE flag (_id integer primary key autoincrement, name varchar(20),fl varchar(20))");

        }
        /*创建表，并判断是否已经存在此表，没创建，则创建并初始化*/
        if (! mysqlhelper.tabIsExist("user")) {
            db.execSQL("CREATE TABLE user (_id integer primary key autoincrement, name varchar(20),password varchar(20))");
        }else {
            Log.i("+++++++++++", "已经创建了，无需再创建");
        }


        init();

    }
    private void init(){
        UserName=(EditText)findViewById(R.id.editText_accountR);
        Key=(EditText)findViewById(R.id.editText_keyR);
        CKey=(EditText)findViewById(R.id.editText_keyR2);
        Register=(Button)findViewById(R.id.button_re);

        Register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name;
        String key;
        String ckey;
        String flag="fl";
        String na="ken";
        ContentValues values;
        ContentValues values1;
        SQLiteDatabase db1;

        boolean creatuser=true;

        switch (v.getId()){
            case R.id.button_re:
                name=UserName.getText().toString();
                key=Key.getText().toString();
                ckey=CKey.getText().toString();
                db1 = mysqlhelper.getReadableDatabase();
                Cursor cursor = db1.query("user", new String[]{"name"}, null, null, null, null, null);
                while (cursor.moveToNext()) {
                if (name.equals(cursor.getString(cursor.getColumnIndex("name")))) {//如果在数据库中找到了该账号，则不允许创建
                    Toast.makeText(this,"该账户已存在", Toast.LENGTH_SHORT).show();
                    creatuser = false;//不允许创建
                    break;
                }
            }
                if(key.equals(ckey) && creatuser==true)
                { db=mysqlhelper.getWritableDatabase();
                    values=new ContentValues();
                    values.put("name",name);
                    values.put("password",key);
                    db.insert("user",null,values);
                    values1=new ContentValues();
                    values1.put("name",name);
                    values1.put("flag",flag);
                    db.insert("flag",null,values1);
                    Toast.makeText(this,"注册成功，请进行登录", Toast.LENGTH_SHORT).show();
                    db.close();
                    Intent intent_login=new Intent(this,login.class);
                    startActivity(intent_login);
                    finish();
                }
                else{
                    Toast.makeText(this,"确认密码不正确，请重试", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



}
