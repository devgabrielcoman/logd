/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.system.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.gabrielcoman.logd.models.Response;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    // constants for the shared preferences file title and key
    private static final String FILE_NAME   = "LOGD_DATA_KEY";
    private static final String KEY_PREFIX  = "KEY_";

    public static void writeToDatabase (Context context, Response response) {

        try {
            context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(KEY_PREFIX + response.getTimestamp(), response.writeToJson().toString())
                    .apply();;
        } catch (Exception e) {
            // do nothing
        }

    }

    public static List<Response> getFromDatabase (Context context) {

        List<Response> responses = new ArrayList<>();

        try {

            SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

            for (String key : preferences.getAll().keySet()) {
                String responseString = preferences.getString(key, null);
                Response response = new Response(responseString);
                responses.add(response);
            }


        } catch (Exception e) {
            // do nothing
        }

        return responses;

    }

}
