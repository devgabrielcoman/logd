/**
 * @Copyright: Gabriel Coman 2017
 * @Author: Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.system.alarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logddatabase.Database;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceQueryRequest;
import com.google.android.gms.awareness.fence.FenceQueryResult;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceStateMap;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func2;

public class AlarmScheduler {

    private static final String alarmKey = "morningeveningKey";
    private static final String morningKey = "morningFenceKey";
    private static final String eveningKey = "eveningFenceKey";

    private static GoogleApiClient client;

    private static void connectClient (Context context) {

        // create client
        client = new GoogleApiClient.Builder(context).addApi(Awareness.API).build();

        // connect it!
        try {
            client.connect();
        } catch (Exception e) {
            // do nothing
        }
    }

    public static Observable<Boolean> scheduleAlarms (Context context) throws SecurityException {

        return Observable.combineLatest(scheduleMorningAlarm(context), scheduleEveningAlarm(context), (morning, evening) -> morning && evening);
//                .subscribe(set -> {
//
//                    if (set) {
//                        DatabaseAPI.setAlarmStatus(context, true);
//                    }
//
//                });

    }

    private static Observable<Boolean> scheduleMorningAlarm (Context context) throws SecurityException {

        if (client == null) {
            connectClient(context);
        }

        Intent intent = new Intent(context, MorningAlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AwarenessFence morningFence = TimeFence.inTimeInterval(TimeFence.TIME_INTERVAL_MORNING);

        FenceUpdateRequest request = new FenceUpdateRequest.Builder().addFence(morningKey, morningFence, pIntent).build();

        return Observable.create(subscriber -> {

            Awareness.FenceApi.updateFences(client, request)
                    .setResultCallback(status -> subscriber.onNext(status.isSuccess()));

        });

    }

    private static Observable<Boolean> scheduleEveningAlarm (Context context) throws SecurityException {

        if (client == null) {
            connectClient(context);
        }

        Intent intent = new Intent(context, EveningAlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AwarenessFence eveningFence = TimeFence.inTimeInterval(TimeFence.TIME_INTERVAL_EVENING);

        FenceUpdateRequest request = new FenceUpdateRequest.Builder().addFence(eveningKey, eveningFence, pIntent).build();

        return Observable.create(subscriber -> {

            Awareness.FenceApi.updateFences(client, request)
                    .setResultCallback(status -> subscriber.onNext(status.isSuccess()));

        });


    }

    public static Observable<Boolean> unscheduleAlarms (Context context) throws SecurityException {

        return Observable.combineLatest(unscheduleMorningAlarm(context), unscheduleEveningAlarm(context), (morning, evening) -> morning && evening);
//                .subscribe(unset -> {
//
//                    if (unset) {
//                        DatabaseAPI.setAlarmStatus(context, false);
//                    }
//
//                });

    }

    private static Observable<Boolean> unscheduleMorningAlarm (Context context) throws SecurityException {

        if (client == null) {
            connectClient(context);
        }

        FenceUpdateRequest request = new FenceUpdateRequest.Builder().removeFence(morningKey).build();

        return Observable.create(subscriber -> {

            Awareness.FenceApi.updateFences(client, request)
                    .setResultCallback(status -> subscriber.onNext(status.isSuccess()));

        });

    }

    private static Observable<Boolean> unscheduleEveningAlarm (Context context) throws SecurityException {

        if (client == null) {
            connectClient(context);
        }

        FenceUpdateRequest request = new FenceUpdateRequest.Builder().removeFence(eveningKey).build();

        return Observable.create(subscriber -> {

            Awareness.FenceApi.updateFences(client, request)
                    .setResultCallback(status -> subscriber.onNext(status.isSuccess()));

        });

    }

    public static boolean areAlarmsSet (Context context) {
        return DatabaseAPI.areAlarmsSet(context);
    }

}
