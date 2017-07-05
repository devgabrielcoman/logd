package com.gabrielcoman.logd.library.profile;

import com.facebook.AccessToken;
import com.gabrielcoman.logd.library.Task;

import rx.Single;

public class CheckLoginTask implements Task <CheckLoginRequest, Void> {

    @Override
    public Single<Void> execute(CheckLoginRequest input) {

        AccessToken token = AccessToken.getCurrentAccessToken();

        return Single.create(subscriber -> {

            if (token != null) {
                subscriber.onSuccess(null);
            } else {
                subscriber.onError(new Throwable());
            }
        });
    }
}
