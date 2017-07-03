package com.gabrielcoman.logd.activities.intro;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
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
            Log.d("Logd", "User already logged in - going to Main");
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
        }
        //
        // goto Login
        else {
            Log.d("Logd", "User not logged in - going to Login");
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
