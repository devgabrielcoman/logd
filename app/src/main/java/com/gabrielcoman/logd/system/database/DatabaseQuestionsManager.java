package com.gabrielcoman.logd.system.database;

import android.content.Context;

import com.gabrielcoman.logd.models.Question;

public class DatabaseQuestionsManager {

    private static final String PREV_FILE_NAME  = "LOGD_PREV_QUESTION_KEY";
    private static final String PREV_QUESTION_PREFIX = "PREV_QUESTION";

    public static void writeQuestionToPreviousDatabase (Context context, Question question) {

        try {
            context.getSharedPreferences(PREV_FILE_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(PREV_QUESTION_PREFIX, question.writeToJson().toString())
                    .apply();
        } catch (Exception e) {
            // do nothing
        }

    }

    public static Question getPreviousQuestion (Context context) {
        try {

            String data = context
                    .getSharedPreferences(PREV_FILE_NAME, Context.MODE_PRIVATE)
                    .getString(PREV_QUESTION_PREFIX, null);

            return new Question(data);
        } catch (Exception e) {
            return null;
        }
    }
}
