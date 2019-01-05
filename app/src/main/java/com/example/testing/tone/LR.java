package com.example.testing.tone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LR extends AppCompatActivity implements View.OnClickListener{


    Button btn_login;
    Button btn_register;
    public static LR instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lr);

        instance=this;

        btn_login = (Button) findViewById(R.id.button_login);
        btn_register = (Button) findViewById(R.id.button_register);


        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_login:
                Intent intent_login=new Intent(this,login.class);
                startActivity(intent_login);
                break;
            case R.id.button_register:
                Intent intent_register=new Intent(this,register.class);
                startActivity(intent_register);
                break;
        }
    }
}

