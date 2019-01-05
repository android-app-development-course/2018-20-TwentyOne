package com.example.testing.tone;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity implements View.OnClickListener {
    MySqlHelper mysqlhelper;
    EditText UserName;
    EditText Key;
    Button Login;
    SQLiteDatabase db;
    String fl="fl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mysqlhelper=new MySqlHelper(this, "information.db", null, 1);
        init();
    }

    private void init(){
        UserName=(EditText)findViewById(R.id.editText_account);
        Key=(EditText)findViewById(R.id.editText_key);
        Login=(Button)findViewById(R.id.button_ok);

        Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name;
        String key;
        ContentValues values;
        boolean err=false;
        boolean mark=false;
        name = UserName.getText().toString();
        key = Key.getText().toString();
        //判断是否输入内容
        if (name.equals("") || key.equals("")) {
            Toast.makeText(this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
        } else {//输入了账号&密码
            db = mysqlhelper.getReadableDatabase();
            Cursor cursor = db.query("user", new String[]{"name", "password"}, null, null, null, null, null);
            //从数据库中匹配账号密码
            while (cursor.moveToNext()) {
                if (name.equals(cursor.getString(cursor.getColumnIndex("name")))
                        && key.equals(cursor.getString(cursor.getColumnIndex("password")))) {
                   mark=true;
                   err=true;
                    break;
                }
            }
            cursor.close();
            if(err==false)
            {
                Toast.makeText(this, "请输入账号或密码错误", Toast.LENGTH_SHORT).show();
            }
            if(mark==true){
                db=mysqlhelper.getWritableDatabase();
                Cursor cursor1 = db.query("flag", new String[]{"name", "fl"}, null, null, null, null, null);
                if(cursor1.getCount()==0){
                    values=new ContentValues();
                    values.put("name", UserName.getText().toString());
                    values.put("fl",fl);
                    db.insert("flag",null,values);
                    LR.instance.finish();
                    Intent intent_main = new Intent(login.this, Home.class);
                    startActivity(intent_main);
                    finish();
                }
               else{
                values=new ContentValues();
                values.put("name", UserName.getText().toString());
                int num=db.update("flag",values,"fl=?",new String[]{fl});
                LR.instance.finish();
                Intent intent_main = new Intent(login.this, Home.class);
                startActivity(intent_main);
                finish();
                }
                cursor1.close();
            }


        }
    }
}

