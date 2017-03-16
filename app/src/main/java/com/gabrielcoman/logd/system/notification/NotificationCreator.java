/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.system.notification;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.models.Question;

public class NotificationCreator {

    public static Notification customNotification (Context context, Question question) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_custom);

        RemoteViews questionTitle = new RemoteViews(context.getPackageName(), R.layout.notification_custom_title);
        questionTitle.setTextViewText(R.id.QuestionTitle, question.getTitle());
        remoteViews.addView(R.id.title, questionTitle);

        for (String answer : question.getAnswers()) {

            RemoteViews rm = new RemoteViews(context.getPackageName(), R.layout.notification_custom_button);
            rm.setTextViewText(R.id.NotificationAnswerText, answer);

            Intent rmIntent = new Intent(context, AnswerButtonListener.class);
            rmIntent.putExtra("answer", answer);
            rmIntent.setAction(Long.toString(System.currentTimeMillis()));
            Log.d("Logd-App", rmIntent.getExtras().toString());
            PendingIntent rmPendingIntent = PendingIntent.getBroadcast(context, 0, rmIntent, PendingIntent.FLAG_ONE_SHOT);

            rm.setOnClickPendingIntent(R.id.NotificationAnswerText, rmPendingIntent);

            remoteViews.addView(R.id.text, rm);
        }

        // pending intent for the journal button
        Intent notificationIntent = new Intent(context, JournalActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(JournalActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pending = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.JournalButton, pending);

        return new NotificationCompat.Builder(context)
                // Set Icon
                .setSmallIcon(R.mipmap.ic_launcher)
                // Set Ticker Message
                .setTicker(context.getString(R.string.notification_ticker_title))
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pending)
                // Set RemoteViews into Notification
                .setContent(remoteViews)
                .setCustomBigContentView(remoteViews)
                .build ();
    }

}
