/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.system.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;

import com.gabrielcoman.logd.activities.AnswerActivity;
import com.gabrielcoman.logd.activities.MainActivity;
import com.gabrielcoman.logd.manager.QuestionManager;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.system.notification.NotificationCreator;

import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Question question = QuestionManager.getNewQuestion(context, new Date());

        Intent notificationIntent = new Intent(context, AnswerActivity.class);
        notificationIntent.putExtra("question", question.writeToJson().toString());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AnswerActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = NotificationCreator.createNotification(context, question.getTitle(), pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }
}
