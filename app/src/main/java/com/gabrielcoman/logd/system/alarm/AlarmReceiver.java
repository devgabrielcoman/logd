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
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.api.QuestionsAPI;
import com.gabrielcoman.logd.system.api.SentimentAPI;

import rx.android.schedulers.AndroidSchedulers;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 2315552;

    @Override
    public void onReceive(Context context, Intent intent) {

        QuestionsAPI.getMorningQuestion(context)
                .subscribe(question -> {

                    Notification notification = customNotification(context, question);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID, notification);

                });
    }

    private Notification customNotification (Context context, Question question) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_custom);

        remoteViews.setTextViewText(R.id.QuestionTitle, question.getTitle());
        remoteViews.setTextViewText(R.id.NotificationAnswerText1, question.getAnswers().get(0));
        remoteViews.setTextViewText(R.id.NotificationAnswerText2, question.getAnswers().get(1));
        remoteViews.setTextViewText(R.id.NotificationAnswerText3, question.getAnswers().get(2));

        Intent intent1 = new Intent(context, NotificationAnswerText1.class);
        intent1.setAction(Long.toString(System.currentTimeMillis()));
        intent1.putExtra("answer1_extra", question.getAnswers().get(0));
        PendingIntent pIntent1 = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_ONE_SHOT);

        Intent intent2 = new Intent(context, NotificationAnswerText2.class);
        intent2.setAction(Long.toString(System.currentTimeMillis()));
        intent2.putExtra("answer2_extra", question.getAnswers().get(1));
        PendingIntent pIntent2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_ONE_SHOT);

        Intent intent3 = new Intent(context, NotificationAnswerText3.class);
        intent3.setAction(Long.toString(System.currentTimeMillis()));
        intent3.putExtra("answer3_extra", question.getAnswers().get(2));
        PendingIntent pIntent3 = PendingIntent.getBroadcast(context, 0, intent3, PendingIntent.FLAG_ONE_SHOT);

        remoteViews.setOnClickPendingIntent(R.id.NotificationAnswerText1, pIntent1);
        remoteViews.setOnClickPendingIntent(R.id.NotificationAnswerText2, pIntent2);
        remoteViews.setOnClickPendingIntent(R.id.NotificationAnswerText3, pIntent3);

        // pending intent for the journal button
        Intent notificationIntent = new Intent(context, JournalActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(JournalActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pending = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.JournalButton, pending);

        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(context.getString(R.string.notification_ticker_title))
                .setAutoCancel(true)
                .setContentIntent(pending)
                .setContent(remoteViews)
                .setCustomBigContentView(remoteViews)
                .build ();
    }

    private static class NotificationAnswerText extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            internalRecv(context, intent, "");
        }

        public void internalRecv (Context context, Intent intent, String extra) {

            Bundle bundle = intent.getExtras();
            String answer = bundle.getString(extra);

            SentimentAPI
                    .analyseSentiment(answer)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(value -> {
                        Response response = new Response(answer, value);
                        DatabaseAPI.writeResponse(context, response);
                        Log.d("Logd-App", "Finished analysing " + answer + " ==> " + value);

                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancel(NOTIFICATION_ID);
                    });

        }
    }

    public static class NotificationAnswerText1 extends NotificationAnswerText {
        @Override
        public void internalRecv(Context context, Intent intent, String extra) {
            super.internalRecv(context, intent, "answer1_extra");
        }
    }

    public static class NotificationAnswerText2 extends NotificationAnswerText {

        @Override
        public void internalRecv(Context context, Intent intent, String extra) {
            super.internalRecv(context, intent, "answer2_extra");
        }
    }

    public static class NotificationAnswerText3 extends NotificationAnswerText {

        @Override
        public void internalRecv(Context context, Intent intent, String extra) {
            super.internalRecv(context, intent, "answer3_extra");
        }
    }
}
