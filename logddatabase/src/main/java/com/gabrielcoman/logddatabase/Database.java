package com.gabrielcoman.logddatabase;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import tv.superawesome.lib.sajsonparser.SAJsonSerializable;

public class Database {

    public static final String     DB_RESPONSE  = "RESPONSE_DB";
    public static final String     DB_QUESTION  = "QUESTION_DB";

    private static final String     ENTRY_PREFIX = "ENTRY_";

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
            } else if (item instanceof SAJsonSerializable) {
                editor.putString(key, ((SAJsonSerializable) item).writeToJson().toString());
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
            JSONObject jsonObject = new JSONObject(json);

            Constructor<?> constructor = cl.getConstructor();
            T instance = (T) constructor.newInstance();
            java.lang.reflect.Method method = cl.getMethod("readFromJson", JSONObject.class);
            method.invoke(instance, jsonObject);

            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    public static Observable<String> getEntries (Context context, String database) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                Map<String, ?> values = context
                        .getSharedPreferences(database, Context.MODE_PRIVATE)
                        .getAll();

                for (String key : values.keySet()) {
                    subscriber.onNext(key);
                }
                subscriber.onCompleted();
            }
        });
    }
}
