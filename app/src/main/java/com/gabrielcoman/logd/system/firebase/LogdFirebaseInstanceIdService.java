package com.gabrielcoman.logd.system.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by gabriel.coman on 03/07/2017.
 */

public class LogdFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        //
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Logd", "Refreshed token: " + refreshedToken);

        //
        // @todo: send to server
    }
}
