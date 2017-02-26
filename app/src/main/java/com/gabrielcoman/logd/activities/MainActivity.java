/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Answer;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.system.alarm.AlarmScheduler;
import com.jakewharton.rxbinding.view.RxView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startNotification = (Button) findViewById(R.id.StartNotification);

        RxView.clicks(startNotification)
                .subscribe(aVoid -> {
                    AlarmScheduler.scheduleAlarm(MainActivity.this);
                });

    }
}
