/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.system.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gabrielcoman.logd.system.api.QuestionsAPI;

import static com.gabrielcoman.logd.system.alarm.NotificationCreator.NOTIFICATION_ID;

public class MorningAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        QuestionsAPI.getMorningQuestion(context)
                .subscribe(question -> {

                    Notification notification = NotificationCreator.customNotification(context, question);
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID, notification);

                });
    }
}
