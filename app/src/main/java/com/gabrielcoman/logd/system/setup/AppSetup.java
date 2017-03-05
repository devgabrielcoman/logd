package com.gabrielcoman.logd.system.setup;

import android.content.Context;
import android.util.Log;

import com.gabrielcoman.logd.system.alarm.AlarmScheduler;
import com.gabrielcoman.logd.system.api.DatabaseAPI;

public class AppSetup {

    public static void scheduleTestAlarm (Context context) {
        AlarmScheduler.scheduleTestAlarm(context);
    }

    public static void setupAlarmsOnFirstOpen (Context context) {

        boolean isAlarmSet = DatabaseAPI.getSetupOnce(context);

        if (!isAlarmSet) {

            Log.d("Logd-App", "Setting up alarms for first time!");

            DatabaseAPI.writeSetupOnce(context);
            AlarmScheduler.scheduleDailyAlarm(context);

        } else {
            Log.d("Logd-App", "Alarms have already been setup");
        }

    }

}
