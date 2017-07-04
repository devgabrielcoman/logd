package com.gabrielcoman.logd.activities.intro;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.login.LoginActivity;
import com.gabrielcoman.logd.activities.main.MainActivity;
import com.gabrielcoman.logd.library.network.NetworkTask;
import com.gabrielcoman.logd.library.network.SendTokenRequest;
import com.gabrielcoman.logd.library.system.ObtainTokenRequest;
import com.gabrielcoman.logd.library.system.ObtainTokenTask;

public class IntroActivity extends BaseActivity {

    ObtainTokenRequest request;
    ObtainTokenTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //
        // send token data at start
        request = new ObtainTokenRequest();
        task = new ObtainTokenTask();
        task.execute(request)
                .flatMap(token -> {
                    SendTokenRequest request = new SendTokenRequest(token);
                    NetworkTask<SendTokenRequest> task = new NetworkTask<>();
                    return task.execute(request);
                })
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
