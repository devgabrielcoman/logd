package com.gabrielcoman.logd.system.api;

import android.content.Context;

import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logddatabase.Database;

import rx.Observable;

public class DatabaseAPI {

    private static final String     RESPONSE_PREFIX = "ENTRY_";
    private static final String     QUESTION_PREFIX = "QUESTION_";

    public static void writeResponse (Context context, Response response) {
        long entry = System.currentTimeMillis()/1000;
        String key = RESPONSE_PREFIX + entry;
        Database.writeItem(context, Database.DB_RESPONSE, key, response);
    }

    public static Observable<Response> getResponses (Context context) {
        return Database.getEntries(context, Database.DB_RESPONSE)
                .map(entry -> Database.getModel(context, Database.DB_RESPONSE, entry, Response.class));
    }

    public static void writeQuestion (Context context, Question question) {
        Database.writeItem(context, Database.DB_QUESTION, QUESTION_PREFIX, question.hashCode());
    }

    public static int getPreviousQuestion (Context context) {
        return Database.getInt(context, Database.DB_QUESTION, QUESTION_PREFIX);
    }
}
