package com.gabrielcoman.logd.system.alarm;

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
        RemoteViews smallContent = new RemoteViews(context.getPackageName(), R.layout.notification_custom_small);
        smallContent.setTextViewText(R.id.QuestionTitle, question);

        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notif)
                .setTicker(context.getString(R.string.notification_ticker_title))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContent(smallContent)
                .build ();
    }

//    public static final int NOTIFICATION_ID = 2315552;
//
//    public static Notification customNotification (Context context, boolean isMorning, Question question) {
//
//        //
//        // answer intent
//        Intent answerIntent = new Intent(context, AnswerActivity.class);
//        answerIntent.putExtra("question", new Gson().toJson(question));
//        answerIntent.putExtra("isMorning", isMorning);
//        TaskStackBuilder answerStackBuilder = TaskStackBuilder.create(context);
//        answerStackBuilder.addParentStack(AnswerActivity.class);
//        answerStackBuilder.addNextIntent(answerIntent);
//
//        PendingIntent pendingAnswer = answerStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        //
//        // Small Notification Content
//        RemoteViews smallContent = new RemoteViews(context.getPackageName(), R.layout.notification_custom_small);
//
//        smallContent.setTextViewText(R.id.QuestionTitle, question.getTitle());
//
//        //
//        // Big Notification Content
//        RemoteViews bigContent = new RemoteViews(context.getPackageName(), R.layout.notification_custom_big);
//
//        bigContent.setTextViewText(R.id.QuestionTitle, question.getTitle());
//        bigContent.setTextViewText(R.id.NotificationAnswerText1, question.getAnswers().get(0));
//        bigContent.setTextViewText(R.id.NotificationAnswerText2, question.getAnswers().get(1));
//        bigContent.setTextViewText(R.id.NotificationAnswerText3, question.getAnswers().get(2));
//
//        Intent intent1 = new Intent(context, NotificationAnswerText1.class);
//        intent1.setAction(Long.toString(System.currentTimeMillis()));
//        intent1.putExtra("answer1_extra", question.getAnswers().get(0));
//        intent1.putExtra("isMorning", isMorning);
//        PendingIntent pIntent1 = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_ONE_SHOT);
//
//        Intent intent2 = new Intent(context, NotificationAnswerText2.class);
//        intent2.setAction(Long.toString(System.currentTimeMillis()));
//        intent2.putExtra("answer2_extra", question.getAnswers().get(1));
//        intent2.putExtra("isMorning", isMorning);
//        PendingIntent pIntent2 = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_ONE_SHOT);
//
//        Intent intent3 = new Intent(context, NotificationAnswerText3.class);
//        intent3.setAction(Long.toString(System.currentTimeMillis()));
//        intent3.putExtra("answer3_extra", question.getAnswers().get(2));
//        intent3.putExtra("isMorning", isMorning);
//        PendingIntent pIntent3 = PendingIntent.getBroadcast(context, 0, intent3, PendingIntent.FLAG_ONE_SHOT);
//
//        bigContent.setOnClickPendingIntent(R.id.NotificationAnswerText1, pIntent1);
//        bigContent.setOnClickPendingIntent(R.id.NotificationAnswerText2, pIntent2);
//        bigContent.setOnClickPendingIntent(R.id.NotificationAnswerText3, pIntent3);
//
//        // pending intent for the journal button
//        Intent journalIntent = new Intent(context, JournalActivity.class);
//        TaskStackBuilder journalStackBuilder = TaskStackBuilder.create(context);
//        journalStackBuilder.addParentStack(JournalActivity.class);
//        journalStackBuilder.addNextIntent(journalIntent);
//
//        PendingIntent pendingJournal = journalStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        bigContent.setOnClickPendingIntent(R.id.JournalButton, pendingJournal);
//
//        return new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.ic_notif)
//                .setTicker(context.getString(R.string.notification_ticker_title))
//                .setAutoCancel(true)
//                .setContentIntent(pendingAnswer)
//                .setContent(smallContent)
//                .setCustomBigContentView(bigContent)
//                .build ();
//    }
//
//    private static class NotificationAnswerText extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            internalRecv(context, intent, "");
//        }
//
//        public void internalRecv (Context context, Intent intent, String extra) {
//
//            Bundle bundle = intent.getExtras();
//            String answer = bundle.getString(extra);
//            boolean isMorning = bundle.getBoolean("isMorning");
//
//            SentimentAPI
//                    .analyseSentiment(answer)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(value -> {
//                        Response response = new Response(answer, value);
//                        if (isMorning) {
//                            DatabaseAPI.writeMorningResponse(context, response);
//                        } else {
//                            DatabaseAPI.writeEveningResponse(context, response);
//                        }
//
//                        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.cancel(NOTIFICATION_ID);
//
//                        Toast.makeText(context, R.string.data_question_answered_toast, Toast.LENGTH_SHORT).show();
//                    });
//
//        }
//    }
//
//    public static class NotificationAnswerText1 extends NotificationAnswerText {
//        @Override
//        public void internalRecv(Context context, Intent intent, String extra) {
//            super.internalRecv(context, intent, "answer1_extra");
//        }
//    }
//
//    public static class NotificationAnswerText2 extends NotificationAnswerText {
//
//        @Override
//        public void internalRecv(Context context, Intent intent, String extra) {
//            super.internalRecv(context, intent, "answer2_extra");
//        }
//    }
//
//    public static class NotificationAnswerText3 extends NotificationAnswerText {
//
//        @Override
//        public void internalRecv(Context context, Intent intent, String extra) {
//            super.internalRecv(context, intent, "answer3_extra");
//        }
//    }
}
