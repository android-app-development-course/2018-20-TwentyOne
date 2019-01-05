package com.example.testing.tone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Bundle bundle=intent.getExtras();
        String thing = intent.getStringExtra("thing");
        int id = intent.getIntExtra("id", 0);
        Log.i("通知：",thing);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                //设置通知标题
                .setSmallIcon(R.drawable.to)
                .setContentTitle("Twenty-One 任务提醒")
                .setTicker("任务提醒")
                .setContentText("记得打卡任务“" + thing + "”喔！")
                .setContentInfo("任务提醒")
                .setAutoCancel(true)//点击后则自动销毁

                //设置通知时间，默认为系统发出通知的时间，通常不用设置
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLights(0xFF0000, 3000, 3000);

        //点击跳到主界面
        Intent intent1 = new Intent(context, Home.class);
        PendingIntent ClickPending = PendingIntent.getActivity(context, 0, intent1, 0);
        mBuilder.setContentIntent(ClickPending);

        Notification notify = mBuilder .build();
        //notify.flags = Notification.FLAG_SHOW_LIGHTS;
        mNotificationManager.notify(id,notify);
    }

}
