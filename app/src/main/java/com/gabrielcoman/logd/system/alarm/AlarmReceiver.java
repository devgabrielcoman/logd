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
import android.os.Parcelable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.os.ParcelableCompat;

import com.gabrielcoman.logd.activities.AnswerActivity;
import com.gabrielcoman.logd.manager.QuestionManager;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.system.database.DatabaseQuestionsManager;
import com.gabrielcoman.logd.system.notification.NotificationCreator;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // get all the questions in the system
        List<Question> questions = QuestionManager.getPossibleQuestions(context);

        // if it exists, get the previously question that's been posed to the user
        Question previousQuestion = DatabaseQuestionsManager.getPreviousQuestion(context);
        // if not null, remove that from the "questions" list
        if (previousQuestion != null) {
            questions.remove(previousQuestion);
        }

        // get the current question from the reduced (or initial) list
        Question currentQuestion = QuestionManager.pickFromList(questions);

        // save the current question as a previous
        DatabaseQuestionsManager.writeQuestionToPreviousDatabase(context, currentQuestion);

        // form intent
        Intent notificationIntent = new Intent(context, AnswerActivity.class);
        notificationIntent.putExtra("question", currentQuestion.writeToJson().toString());

        // create the stack w/ the notification intents
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AnswerActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        // form pending intent
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // create notification
        Notification notification = NotificationCreator.createNotification(context, currentQuestion.getTitle(), pendingIntent);

        // start notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }
}
