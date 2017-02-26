/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.system.notification;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.gabrielcoman.logd.R;

public class NotificationCreator {

    public static Notification createNotification (Context context, String text, PendingIntent pendingIntent) {
        return new NotificationCompat
                .Builder(context)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(text)
                .setTicker(context.getString(R.string.notification_ticker_title))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build();
    }

}
