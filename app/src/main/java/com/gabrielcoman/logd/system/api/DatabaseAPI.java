package com.gabrielcoman.logd.system.api;

import android.content.Context;
import android.util.Log;

import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logddatabase.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseAPI {

    private static final String      DB_STARTUP  = "DB_STARTUP";

    private static final String      DB_MORNING  = "DB_MORNING";
    private static final String      DB_EVENING  = "DB_EVENING";
    private static final String      DB_JOURNAL  = "DB_JOURNAL";

    private static final String      DB_SETUP     = "DB_SETUP";
    private static final String      DB_NEVERSHOW = "DB_NEVERSHOW";

    private static final String     STARTUP_KEY      = "STARTUP_KEY";
    private static final String     NEVERSHOW_KEY    = "NEVERSHOW";
    private static final String     SETUP_KEY        = "SETUP_KEY";

    public static void writeMorningResponse (Context context, Response response) {

        String key = getCalendarTypeKey();

        Log.d("Logd-App", "Writing Morning Response for " + key + " with " + response.getAnswer());

        Database.writeItem(context, DB_MORNING, key, response);
    }

    public static void writeEveningResponse (Context context, Response response) {

        String key = getCalendarTypeKey();

        Log.d("Logd-App", "Writing Evening Response for " + key + " with " + response.getAnswer());

        Database.writeItem(context, DB_EVENING, key, response);
    }

    public static void writeJournalResponse (Context context, Response response) {

        String key = "KEY_" + System.currentTimeMillis();

        Log.d("Logd-App", "Writing Journal Response for " + key + " with " + response.getAnswer());

        Database.writeItem(context, DB_JOURNAL, key, response);
    }

    public static List<Response> getResponses (Context context) {
        List<String> morningKeys = Database.getEntries(context, DB_MORNING);
        List<String> eveningKeys = Database.getEntries(context, DB_EVENING);
        List<String> journalKeys = Database.getEntries(context, DB_JOURNAL);
        List<Response> responses = new ArrayList<>();
        for (String key : morningKeys) {
            responses.add(Database.getModel(context, DB_MORNING, key, Response.class));
        }
        for (String key : eveningKeys) {
            responses.add(Database.getModel(context, DB_EVENING, key, Response.class));
        }
        for (String key : journalKeys) {
            responses.add(Database.getModel(context, DB_JOURNAL, key, Response.class));
        }

        Log.d("Logd-App", "Got " + morningKeys.size() + " morning, " + eveningKeys.size() + " evening and " + journalKeys.size() + " journal responses ==> " + responses.size() + " total responses");

        return responses;
    }

    public static boolean hasMorningResponseForToday (Context context) {

        String key = getCalendarTypeKey();

        return Database.getModel(context, DB_MORNING, key, Response.class) != null;
    }

    public static boolean hasEveningResponseForToday (Context  context) {

        String key = getCalendarTypeKey();

        return Database.getModel(context, DB_EVENING, key, Response.class) != null;

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

    private static String getCalendarTypeKey () {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return day + "_" + month + "_" + year;
    }

    public static void writeAppStarted (Context context) {
        Database.writeItem(context, DB_STARTUP, STARTUP_KEY, true);
    }

    public static boolean firstStart (Context context) {
        return !Database.getBool(context, DB_STARTUP, STARTUP_KEY);
    }
}
