package com.gabrielcoman.logd.system.register;

import android.os.Handler;
import android.util.Log;

import com.facebook.Profile;
import com.gabrielcoman.logd.models.Token;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.annotations.Since;

import rx.Single;
import rx.SingleSubscriber;

public class RegisterSystem {

    private final int DELAY = 1000;
    private Handler handler = new Handler();
    private Runnable runnable = null;

    public Single<Token> execute () {
        return Single.create(singleSubscriber -> {

            runnable = () -> {

                String token = FirebaseInstanceId.getInstance().getToken();
                Profile profile = Profile.getCurrentProfile();

                if (token != null && profile != null) {
                    singleSubscriber.onSuccess(new Token(profile.getId(), token));
                } else {
                    Log.d("Logd", "String trying to get Firebase token && fb profile ...");
                    handler.postDelayed(runnable, DELAY);
                }
            };
            handler.postDelayed(runnable, DELAY);

        });
    }

    public interface Interface {
        void gotData(String fbId, String token);
    }
}
