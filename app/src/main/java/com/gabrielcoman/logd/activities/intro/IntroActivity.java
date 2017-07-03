package com.gabrielcoman.logd.activities.intro;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.login.LoginActivity;
import com.gabrielcoman.logd.activities.main.MainActivity;
import com.gabrielcoman.logd.models.Token;
import com.gabrielcoman.logd.network.SendTokenData;
import com.gabrielcoman.logd.system.register.RegisterSystem;

import okhttp3.Response;
import rx.Single;
import rx.functions.Action1;
import rx.functions.Func1;

public class IntroActivity extends BaseActivity {

    SendTokenData sendTokenData;
    RegisterSystem registerSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //
        // send token data at start
        registerSystem = new RegisterSystem();
        sendTokenData = new SendTokenData();
        registerSystem.execute()
                .flatMap(token -> sendTokenData.execute(token))
                .subscribe(response -> Log.d("Logd", "Sent network data!"));
    }

    @Override
    protected void onStart() {
        super.onStart();

        AccessToken token = AccessToken.getCurrentAccessToken();

        //
        // goto Main
        if (token != null) {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
        }
        //
        // goto Login
        else {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
