package com.example.testing.tone;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Edit extends Activity implements View.OnClickListener{

    private Button dateChoose;
    private Button done;
    private Button back;

    private TextView dateDisplay;
    private TextView HM;
    private EditText mEtThing;
    private EditText mEtEncourage;
    int mYear, mMonth, mDay;
    final int DATE_DIALOG = 1;
    private MyHelper myHelper;
    private ContentValues values;

    SQLiteDatabase db;
    private String time;
    private String encourage;
    private String thing;
    private String _id;//获取的id
    private TimePickerDialog timePickerDialog;
    private int hour;
    private int minutes;
    private String dateStr;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initView();

        Intent i=getIntent();//因为Edit是通过intend来启动的，所以通过getIntend来获取与这个Activity相关的数据
        _id = i.getStringExtra("_id");
        db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("information",null,"_id=?",new String[]{_id},null,null,null);
        cursor.moveToFirst();
        thing = cursor.getString(1);
        encourage = cursor.getString(2);
        time = cursor.getString(3);
        String _hour = cursor.getString(5);
        hour = Integer.parseInt(_hour);
        String _minutes = cursor.getString(6);
        minutes = Integer.parseInt(_minutes);
        dateStr = _hour+":"+ _minutes;
        mEtThing.setText(thing);
        mEtEncourage.setText(encourage);
        dateDisplay.setText(time);
        HM.setText(dateStr);
    }


    public void initView(){
        dateChoose = (Button) findViewById(R.id.dateChoose);
        dateChoose.setOnClickListener(this);
        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(this);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
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
            case R.id.back:
                finish();
                break;
            case R.id.done:
                thing = mEtThing.getText().toString();
                encourage = mEtEncourage.getText().toString();
                time = dateDisplay.getText().toString();
                db = myHelper.getWritableDatabase();
                db.execSQL("UPDATE information SET thing = \"" + thing + "\" , encourage = \"" + encourage + "\" , time = \"" + time + "\" , hour = \"" + hour +"\" , minutes = \"" + minutes + "\" WHERE _id = "+_id);
                Toast.makeText(Edit.this, "修改成功！", Toast.LENGTH_SHORT).show();
                setResult(2);
                finish();
                break;
            case R.id.HM:
                showTime();
                break;
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

    public void display() {
        dateDisplay.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
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
