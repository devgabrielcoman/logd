package com.gabrielcoman.logd.library.system;

import android.os.Handler;
import android.util.Log;

import com.facebook.Profile;
import com.gabrielcoman.logd.library.Task;
import com.gabrielcoman.logd.models.Token;
import com.google.firebase.iid.FirebaseInstanceId;

import rx.Single;

public class ObtainTokenTask implements Task <ObtainTokenRequest, Single<Token>> {

    private final int DELAY = 1000;
    private Handler handler = new Handler();
    private Runnable runnable = null;

    @Override
    public Single<Token> execute(ObtainTokenRequest obtainTokenRequest) {
        return Single.create(singleSubscriber -> {

            runnable = () -> {

                String token = FirebaseInstanceId.getInstance().getToken();
                Profile profile = Profile.getCurrentProfile();

                if (token != null && profile != null) {
                    singleSubscriber.onSuccess(new Token(profile.getId(), token));
                } else {
                    Log.d("Logd", "Trying to get Firebase token && Facebook profile ...");
                    handler.postDelayed(runnable, DELAY);
                }
            };
            handler.postDelayed(runnable, DELAY);

        });
    }
}
