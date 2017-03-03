/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.alarm.AlarmScheduler;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.Observable;

public class MainActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 111;

    private Observable<List<Response>> responses;
    private RxDataSource<Response> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String questionStr = bundle.getString("question");
            Intent mainIntent = new Intent(this, AnswerActivity.class);
            mainIntent.putExtra("question", questionStr);
            startActivityForResult(mainIntent, SET_REQ_CODE);
        }

        Button startNotification = (Button) findViewById(R.id.StartNotification);

        RxView.clicks(startNotification)
                .subscribe(aVoid -> {
                    AlarmScheduler.scheduleAlarm(MainActivity.this);
                });

        ListView history = (ListView) findViewById(R.id.History);

        responses = DatabaseAPI.getResponses(this).toSortedList();

        dataSource = RxDataSource.create(this);
        dataSource.bindTo(history)
                .customiseRow(R.layout.row_history, Response.class, (response, view) -> {

                    TextView sentimentTextView = (TextView) view.findViewById(R.id.ResponseSentiment);
                    sentimentTextView.setText("" + response.getValue());

                    TextView dateTextView = (TextView) view.findViewById(R.id.ResponseDate);
                    dateTextView.setText(response.getDate());

                    TextView textTextView = (TextView) view.findViewById(R.id.ResponseText);
                    textTextView.setText(response.getAnswer());
                });

        // update
        updateData();

        // what happens when some activity closes towards this one
        setOnActivityResult(this::updateData);
    }

    private void updateData () {
        responses.subscribe(responses -> dataSource.update(responses));
    }
}
