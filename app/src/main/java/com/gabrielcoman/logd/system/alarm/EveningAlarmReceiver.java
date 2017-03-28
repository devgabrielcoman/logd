package com.gabrielcoman.logd.system.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.api.QuestionsAPI;

import static com.gabrielcoman.logd.system.alarm.NotificationCreator.NOTIFICATION_ID;

public class EveningAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        QuestionsAPI.getEveningQuestion(context)
                .subscribe(question -> {

                    if (!DatabaseAPI.hasEveningResponseForToday(context)) {

                        Log.d("Logd-App", "User needs Evening Question, will ask");

                        Notification notification = NotificationCreator.customNotification(context, false, question);
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(NOTIFICATION_ID, notification);
                    } else {
                        Log.d("Logd-App", "User has already responded to Evening Question, won't ask again");
                    }

                });
    }
}
