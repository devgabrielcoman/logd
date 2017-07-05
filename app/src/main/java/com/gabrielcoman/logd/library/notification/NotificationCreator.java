package com.gabrielcoman.logd.library.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.answer.AnswerActivity;

public class NotificationCreator {

    private static final int NOTIFICATION_ID = 2315552;

    public void fireNotification (Context context, Notification notification) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public Notification createNotification (Context context, boolean isMorning, String question) {

        //
        // intent
        Intent intent = new Intent(context, AnswerActivity.class);
        intent.putExtra("question", question);
        intent.putExtra("isMorning", isMorning);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AnswerActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews smallContent = new RemoteViews(context.getPackageName(), R.layout.notification);
        smallContent.setTextViewText(R.id.QuestionTitle, question);

        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notif)
                .setTicker(context.getString(R.string.notification_ticker_title))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContent(smallContent)
                .build ();
    }
}
