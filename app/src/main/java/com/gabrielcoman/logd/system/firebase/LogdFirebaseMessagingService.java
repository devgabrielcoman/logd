package com.gabrielcoman.logd.system.firebase;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import com.gabrielcoman.logd.system.alarm.NotificationCreator;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class LogdFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationCreator creator;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Logd", "From: " + remoteMessage.getFrom());



        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //
            // get data
            Map<String, String> data = remoteMessage.getData();

            //
            // get data elements
            String question = "N/A";
            try {
                question = data.get("question");
            } catch (NullPointerException e) {
                // do nothing
            }

            String title = "N/A";
            try {
                title = data.get("title");
            } catch (NullPointerException e) {
                // do nothing
            }

            Boolean isMorning = true;
            try {
                String sIsMorning = data.get("isMorning");
                isMorning = Boolean.getBoolean(sIsMorning);
            } catch (NullPointerException e) {
                // do nothing
            }

            //
            // invoke notification
            Context context = getApplicationContext();
            creator = new NotificationCreator();
            Notification notification = creator.createNotification(context, isMorning, question);
            creator.fireNotification(context, notification);
        }
    }
}
