package com.gabrielcoman.logd.activities.intro;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.login.LoginActivity;
import com.gabrielcoman.logd.activities.main.MainActivity;
import com.gabrielcoman.logd.library.network.NetworkTask;
import com.gabrielcoman.logd.library.network.SendTokenRequest;
import com.gabrielcoman.logd.library.profile.CheckLoginRequest;
import com.gabrielcoman.logd.library.profile.CheckLoginTask;
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
        // set font
        TextView mainText = (TextView) findViewById(R.id.MainText);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/junegull.ttf");
        mainText.setTypeface(typeface);

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

        //
        // check to see if user is logged in or not
        CheckLoginRequest request = new CheckLoginRequest();
        CheckLoginTask task = new CheckLoginTask();
        task.execute(request)
                .subscribe(aVoid -> {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                }, throwable -> {
                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent);
                });
    }
}
