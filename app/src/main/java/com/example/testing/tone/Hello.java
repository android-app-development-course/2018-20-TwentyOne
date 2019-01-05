package com.example.testing.tone;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Hello extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    setContentView(R.layout.activity_hello);
    Thread myThread=new Thread(){//创建子线程
        @Override
        public void run() {
            try{
                sleep(3000);//使程序休眠五秒
                Intent it=new Intent(getApplicationContext(),LR.class);//启动MainActivity
                startActivity(it);
                finish();//关闭当前活动
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
        myThread.start();//启动线程
}
}
