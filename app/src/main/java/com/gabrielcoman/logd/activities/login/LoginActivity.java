package com.gabrielcoman.logd.activities.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
        // set font
        TextView mainText = (TextView) findViewById(R.id.MainText);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/junegull.ttf");
        mainText.setTypeface(typeface);

        //
        // initialize Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //
        // create request & task
        LoginRequest request = new LoginRequest();
        LoginTask task = new LoginTask(LoginActivity.this);

        //
        // handle clicks
        RxView.clicks(loginButton)
                .subscribe(aVoid -> {

                    loginButton.setEnabled(false);

                    task.execute(request)
                            .doOnError(throwable -> loginButton.setEnabled(true))
                            .subscribe(aVoid1 -> {
                                loginButton.setEnabled(true);
                            }, throwable -> {

                                SAAlert.getInstance().show(
                                        this,
                                        getString(R.string.activity_login_error_title),
                                        getString(R.string.activity_login_error_message),
                                        getString(R.string.activity_login_error_tryagain),
                                        null,
                                        false,
                                        0,
                                        null);

                            }, () -> {
                                Intent intent = new Intent(LoginActivity.this, SetupActivity.class);
                                startActivity(intent);
                            });
                });

        setOnActivityResult(task::callbackResult);
    }
}
