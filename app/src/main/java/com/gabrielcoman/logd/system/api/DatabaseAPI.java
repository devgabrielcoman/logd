package com.gabrielcoman.logd.system.api;

import android.content.Context;
import android.util.Log;

import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logddatabase.Database;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAPI {

    public static final String      DB_RESPONSE  = "RESPONSE_DB";
    public static final String      DB_SETUP     = "DB_SETUP";
    public static final String      DB_NEVERSHOW = "DB_NEVERSHOW";

    private static final String     RESPONSE_PREFIX = "ENTRY_";
    private static final String     NEVERSHOW_KEY    = "NEVERSHOW";
    private static final String     SETUP_KEY        = "SETUP_KEY";

    public static void writeResponse (Context context, Response response) {
        long entry = System.currentTimeMillis()/1000;
        String key = RESPONSE_PREFIX + entry;
        Database.writeItem(context, DB_RESPONSE, key, response);
    }

    public static List<Response> getResponses (Context context) {
        List<String> keys = Database.getEntries(context, DB_RESPONSE);
        List<Response> responses = new ArrayList<>();
        for (String key : keys) {
            responses.add(Database.getModel(context, DB_RESPONSE, key, Response.class));
        }
        return responses;
    }

    public static void writeNeverShowPermissions (Context context) {
        Database.writeItem(context, DB_NEVERSHOW, NEVERSHOW_KEY, true);
    }

    public static boolean shouldNeverShowPermissions (Context context) {
        return Database.getBool(context, DB_NEVERSHOW, NEVERSHOW_KEY);
    }

    public static void setAlarmStatus (Context context, boolean value) {
        Database.writeItem(context, DB_SETUP, SETUP_KEY, value);
    }

    public static boolean areAlarmsSet (Context context) {
        return Database.getBool(context, DB_SETUP, SETUP_KEY);
    }
}
