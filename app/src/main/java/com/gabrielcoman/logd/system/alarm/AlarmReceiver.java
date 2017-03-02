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
import android.util.Log;

import com.gabrielcoman.logd.activities.AnswerActivity;
import com.gabrielcoman.logd.manager.QuestionManager;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.notification.NotificationCreator;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int previousQuestionHash = DatabaseAPI.getPreviousQuestion(context);

        QuestionManager.getPossibleQuestions(context)
                .filter(question -> question.hashCode() != previousQuestionHash)
                .toList()
                .subscribe(questions -> {

                    // pick a random question from the list
                    Question pickedQuestion = QuestionManager.pickFromList(questions);

                    Log.d("Logd-App", "Got " + questions.size() + " questions in the end (without " + previousQuestionHash + "). Picked question " + pickedQuestion.getTitle() + " with hash " + pickedQuestion.hashCode());

                    // update the database
                    DatabaseAPI.writeQuestion(context, pickedQuestion);

                    // form intent
                    Intent notificationIntent = new Intent(context, AnswerActivity.class);
                    notificationIntent.putExtra("question", pickedQuestion.writeToJson().toString());

                    // create the stack w/ the notification intents
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addParentStack(AnswerActivity.class);
                    stackBuilder.addNextIntent(notificationIntent);

                    // form pending intent
                    PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    // create notification
                    Notification notification = NotificationCreator.createNotification(context, pickedQuestion.getTitle(), pendingIntent);

                    // start notification
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, notification);
                });
    }
}
