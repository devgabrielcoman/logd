package com.gabrielcoman.logd.system.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.api.SentimentAPI;

import rx.android.schedulers.AndroidSchedulers;

public class AnswerButtonListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String answer = bundle.getString("answer");

        Log.d("Logd-App", "Bundle " + bundle.toString());
        Log.d("Logd-App", "Starting to analyse " + answer);

        SentimentAPI
                .analyseSentiment(answer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    Response response = new Response(answer, value);
                    DatabaseAPI.writeResponse(context, response);
                    Log.d("Logd-App", "Finished analysing " + answer + " ==> " + value);

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(2315552);
                });
    }
}
