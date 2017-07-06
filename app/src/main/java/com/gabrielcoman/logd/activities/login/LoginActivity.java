package com.gabrielcoman.logd.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.setup.SetupActivity;
import com.gabrielcoman.logd.library.profile.LoginRequest;
import com.gabrielcoman.logd.library.profile.LoginTask;
import com.jakewharton.rxbinding.view.RxView;

import tv.superawesome.lib.sautils.SAAlert;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //
        // get elements
        Button loginButton = (Button) findViewById(R.id.LoginButton);

        //
        // initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        LoginRequest request = new LoginRequest();
        LoginTask task = new LoginTask(LoginActivity.this);

        RxView.clicks(loginButton)
                .subscribe(aVoid -> {

                    loginButton.setEnabled(false);

                    task.execute(request)
                            .subscribe(token -> {

                                if (token != null) {
                                    Intent intent = new Intent(LoginActivity.this, SetupActivity.class);
                                    startActivity(intent);
                                } else {
                                    loginButton.setEnabled(true);
                                }

                            }, throwable -> {

                                loginButton.setEnabled(true);

                                SAAlert.getInstance().show(
                                        this,
                                        getString(R.string.activity_login_error_title),
                                        getString(R.string.activity_login_error_message),
                                        getString(R.string.activity_login_error_tryagain),
                                        null,
                                        false,
                                        0,
                                        null);

                            });
                });

        setOnActivityResult(task::callbackResult);
    }
}
