package com.gabrielcoman.logd.library.profile;

import android.app.Activity;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gabrielcoman.logd.library.Task;

import rx.Observable;

public class LoginTask implements Task<LoginRequest, Observable<Void>> {

    private Activity activity;
    private CallbackManager manager;

    public LoginTask(Activity context) {
        manager = CallbackManager.Factory.create();
        activity = context;
    }

    @Override
    public Observable<Void> execute(LoginRequest input) {

        return Observable.create(subscriber -> {

            LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    subscriber.onCompleted();
                }

                @Override
                public void onCancel() {
                    subscriber.onNext(null);
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
