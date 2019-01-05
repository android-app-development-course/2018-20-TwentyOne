package com.example.testing.tone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hhhqzh on 2018/12/14.
 */

//数据库
class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context){
        super(context,"information.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS information(_id INTEGER PRIMARY KEY AUTOINCREMENT,thing VARCHAR(20),encourage VARCHAR(20),time VARCHAR(20),flag INTEGER ,hour INTEGER , minutes INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
