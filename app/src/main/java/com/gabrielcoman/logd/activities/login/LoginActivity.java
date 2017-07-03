package com.gabrielcoman.logd.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.setup.SetupActivity;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements FacebookCallback<LoginResult> {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //
        // initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //
        // create the callback manager and register a login callback
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //
    // start the login action
    public void loginAction(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // FacebookCallback<LoginResult> implementation
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onSuccess(LoginResult loginResult) {
        Intent intent = new Intent(LoginActivity.this, SetupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCancel() {
        // do nothing
    }

    @Override
    public void onError(FacebookException error) {
        // @todo: show error
    }
}
