/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.system.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmScheduler {

    private static PendingIntent sendBroadcast (Context context) {

        Intent intent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        intent.addCategory("android.intent.category.DEFAULT");

        return PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public static void scheduleTestAlarm (Context context) {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent broadcast = sendBroadcast(context);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 15);
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }

    public static void scheduleDailyAlarm (Context context) {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent broadcast = sendBroadcast(context);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);

    }

}
