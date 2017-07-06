package com.gabrielcoman.logd.library.profile;

import android.app.Activity;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gabrielcoman.logd.library.Task;

import rx.Single;

public class LoginTask implements Task<LoginRequest, String> {

    private Activity activity;
    private CallbackManager manager;

    public LoginTask(Activity context) {
        manager = CallbackManager.Factory.create();
        activity = context;
    }

    @Override
    public Single<String> execute(LoginRequest input) {

        return Single.create(subscriber -> {

            LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    subscriber.onSuccess(loginResult.getAccessToken().getToken());
                }

                @Override
                public void onCancel() {
                    subscriber.onSuccess(null);
                }

                @Override
                public void onError(FacebookException error) {
                    subscriber.onError(error);
                }
            });

            //
            // start login process
            LoginManager.getInstance().logInWithReadPermissions(activity, input.permissions);
        });
    }

    public void callbackResult(int requestCode, int resultCode, Intent data) {
        manager.onActivityResult(requestCode, resultCode, data);
    }
}
