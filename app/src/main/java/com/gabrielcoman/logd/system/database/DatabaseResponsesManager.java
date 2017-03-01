/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.system.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DatabaseResponsesManager {

    // constants for the shared preferences file title and key
    private static final String FILE_NAME       = "LOGD_DATA_KEY";
    private static final String KEY_PREFIX      = "KEY_";

    public static void writeResponse (Context context, Response response) {

        try {
            context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(KEY_PREFIX + response.getTimestamp(), response.writeToJson().toString())
                    .apply();
        } catch (Exception e) {
            // do nothing
        }

    }

    public static List<Response> getResponses (Context context) {

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

        Collections.sort(responses, (o1, o2) -> {
            if (o1.getTimestamp() > o2.getTimestamp()) return -1;
            if (o1.getTimestamp() < o2.getTimestamp()) return 1;
            return 0;
        });

        return responses;

    }


}
