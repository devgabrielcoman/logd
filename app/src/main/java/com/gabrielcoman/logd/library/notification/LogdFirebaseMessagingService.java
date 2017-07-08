package com.gabrielcoman.logd.library.notification;

import android.app.Notification;
import android.content.Context;
import android.util.Log;

import com.gabrielcoman.logd.library.notification.NotificationCreator;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class LogdFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

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
                isMorning = sIsMorning.equals("true");
            } catch (NullPointerException e) {
                // do nothing
            }

            //
            // invoke notification
            Context context = getApplicationContext();
            NotificationCreator creator = new NotificationCreator();
            Notification notification = creator.createNotification(context, isMorning, question);
            creator.fireNotification(context, notification);
        }
    }
}
