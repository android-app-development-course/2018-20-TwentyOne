package com.example.testing.tone;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ADD extends Activity implements View.OnClickListener{

    private Button end;
    private  Button down;
    private TextView dateDisplay;
    private TextView HM;
    private EditText mEtThing;
    private EditText mEtEncourage;
    int mYear, mMonth, mDay;
    final int DATE_DIALOG = 1;
    private MyHelper myHelper;
    private ContentValues values;
    private int hour;
    private int minutes;
    private TimePickerDialog timePickerDialog;
    private String dateStr;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initView();

    }

    void initView(){
        end = (Button) findViewById(R.id.dateChoose);
        end.setOnClickListener(this);
        down = (Button) findViewById(R.id.done);
        down.setOnClickListener(this);
        mEtThing = (EditText) findViewById(R.id.thing);
        mEtEncourage = (EditText) findViewById(R.id.encourage);
        dateDisplay = (TextView) findViewById(R.id.dateDisplay);
        HM = (TextView) findViewById(R.id.HM);
        HM.setOnClickListener(this);

        myHelper = new MyHelper(this);
        values = new ContentValues();

        calendar = Calendar.getInstance();
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dateChoose:
                showDialog(DATE_DIALOG);
                break;
            case R.id.done:
                SQLiteDatabase db = myHelper.getWritableDatabase();
                String thing = mEtThing.getText().toString();
                String encourage = mEtEncourage.getText().toString();
                String time = dateDisplay.getText().toString();
                int flag = 0;
                values.put("thing",thing);
                values.put("encourage",encourage);
                values.put("time",time);
                values.put("flag",flag);
                values.put("hour",hour);
                values.put("minutes",minutes);
                db.insert("information",null,values);
                Intent it=new Intent(this,Home.class);
                startActivity(it);
                finish();
                break;
            case R.id.HM:
                showTime();
        }
    }

    //日历
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    //显示日期
    public void display() {
        dateDisplay.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
    }


    //显示时间
    public void showTime(){
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minutes = minute;
                dateStr=Integer.toString(hour) +":"+ Integer.toString(minutes);
                HM.setText(dateStr);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }
}


