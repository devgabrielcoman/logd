/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.rxdatasource.RxDataSource;
import com.gabrielcoman.logd.system.alarm.AlarmScheduler;
import com.gabrielcoman.logd.system.database.DatabaseManager;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

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

        ListView history = (ListView) findViewById(R.id.History);

        List<Response> responses = DatabaseManager.getFromDatabase(this);

        RxDataSource.from(MainActivity.this, responses)
                .bindTo(history)
                .customiseRow(R.layout.row_history, Response.class, (response, view) -> {

                    TextView dateTextView = (TextView) view.findViewById(R.id.ResponseDate);
                    dateTextView.setText(response.getDate());

                    TextView textTextView = (TextView) view.findViewById(R.id.ResponseText);
                    textTextView.setText(response.getAnswer().getTitle());
                })
                .update();

    }
}
