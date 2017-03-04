package com.gabrielcoman.logd.system.api;

import android.content.Context;
import android.util.Log;

import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logddatabase.Database;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAPI {

    private static final String     RESPONSE_PREFIX = "ENTRY_";
    private static final String     QUESTION_PREFIX = "QUESTION_";

    public static void writeResponse (Context context, Response response) {
        long entry = System.currentTimeMillis()/1000;
        String key = RESPONSE_PREFIX + entry;
        Database.writeItem(context, Database.DB_RESPONSE, key, response);
    }

    public static List<Response> getResponses (Context context) {
        List<String> keys = Database.getEntries(context, Database.DB_RESPONSE);
        List<Response> responses = new ArrayList<>();
        for (String key : keys) {
            responses.add(Database.getModel(context, Database.DB_RESPONSE, key, Response.class));
        }
        Log.d("Logd-App", responses.toString());
        return responses;
    }

    public static void writeQuestion (Context context, Question question) {
        Database.writeItem(context, Database.DB_QUESTION, QUESTION_PREFIX, question.hashCode());
    }

    public static int getPreviousQuestion (Context context) {
        return Database.getInt(context, Database.DB_QUESTION, QUESTION_PREFIX);
    }
}
