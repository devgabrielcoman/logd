package com.gabrielcoman.logd.activities.intro;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.login.LoginActivity;
import com.gabrielcoman.logd.activities.main.MainActivity;

public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
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
