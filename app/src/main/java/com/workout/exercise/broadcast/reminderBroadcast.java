package com.workout.exercise.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.workout.exercise.activity.MainActivity;
import com.workout.exercise.a7minuteworkout.R;

import java.util.Random;

/**
 * Created by appsb on 24-07-2018.
 */

public class reminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Random r=new Random();
        Intent i = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,"111")
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText("7 Min Workout")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.logo)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            NotificationChannel channel= new NotificationChannel("111","Reminder", NotificationManager.IMPORTANCE_DEFAULT);
//            channel.enableVibration(true);
//            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),null);
//            notificationManager.createNotificationChannel(channel);
//        }

        notificationManager.notify(r.nextInt(10000),notificationBuilder.build());
    }
}
