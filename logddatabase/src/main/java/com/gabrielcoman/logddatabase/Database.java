package com.gabrielcoman.logddatabase;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Database {

    public static <T> void writeItem (Context context, String database, String key, T item) {

        try {

            SharedPreferences preferences = context.getSharedPreferences(database, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            if (item instanceof Integer) {
                editor.putInt(key, (Integer) item);
            } else if (item instanceof Boolean) {
                editor.putBoolean(key, (Boolean) item);
            } else if (item instanceof Float) {
                editor.putFloat(key, (Float) item);
            } else if (item instanceof Long) {
                editor.putLong(key, (Long) item);
            } else if (item instanceof String) {
                editor.putString(key, (String) item);
            } else if (item != null && item instanceof Object) {
                String json = new Gson().toJson(item);
                editor.putString(key, json);
            }

            editor.apply();

        } catch (Exception e) {
            // do nothing
        }
    }

    private static <T> T getItemInternal (Context context, String database, String key) {
        return (T) context
                .getSharedPreferences(database, Context.MODE_PRIVATE)
                .getAll()
                .get(key);
    }

    public static int getInt (Context context, String database, String key) {
        try {
            return getItemInternal(context, database, key);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean getBool (Context context, String database, String key) {
        try {
            return getItemInternal(context, database, key);
        } catch (Exception e) {
            return false;
        }
    }

    public static float getFloat (Context context, String database, String key) {
        try {
            return getItemInternal(context, database, key);
        } catch (Exception e) {
            return 0.0F;
        }
    }

    public static long getLong (Context context, String database, String key) {
        try {
            return getItemInternal(context, database, key);
        } catch (Exception e) {
            return 0L;
        }
    }

    public static String getString (Context context, String database, String key) {
        try {
            return getItemInternal(context, database, key);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getModel (Context context, String database, String key, Class<T> cl) {
        try {
            String json = getString(context, database, key);
            return new Gson().fromJson(json, cl);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> getEntries (Context context, String database) {
        Map<String, ?> values = context.getSharedPreferences(database, Context.MODE_PRIVATE).getAll();
        List<String> result = new ArrayList<>();
        result.addAll(values.keySet());
        return result;
    }
}
