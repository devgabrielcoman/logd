/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.answer.AnswerActivity;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.alarm.AlarmScheduler;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.subjects.PublishSubject;

public class MainActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startNotification = (Button) findViewById(R.id.StartNotification);

        ListView history = (ListView) findViewById(R.id.History);

        getStringExtras("question")
                .subscribe(s -> {
                    Intent mainIntent = new Intent(MainActivity.this, AnswerActivity.class);
                    mainIntent.putExtra("question", s);
                    startActivityForResult(mainIntent, SET_REQ_CODE);
                });

        RxView.clicks(startNotification)
                .subscribe(aVoid -> {
                    AlarmScheduler.scheduleAlarm(MainActivity.this);
                });

        PublishSubject<List<Response>> subject = PublishSubject.create();

        subject.asObservable()
                .startWith(DatabaseAPI.getResponses(this))
                .map(responses -> {
                    List<ResponseViewModel> viewModels = new ArrayList<>();
                    for (Response r: responses) {
                        viewModels.add(new ResponseViewModel(r));
                    }
                    Collections.sort(viewModels);
                    return viewModels;
                })
                .subscribe(models -> {

                    RxDataSource.create(MainActivity.this)
                            .bindTo(history)
                            .customiseRow(R.layout.row_history, ResponseViewModel.class, (model, view) -> {

                                ((TextView) view.findViewById(R.id.ResponseSentiment)).setText(model.getValue());
                                ((TextView) view.findViewById(R.id.ResponseDate)).setText(model.getDate());
                                ((TextView) view.findViewById(R.id.ResponseText)).setText(model.getAnswer());

                            })
                            .update(models);

                });

        setOnActivityResult(() -> subject.onNext(DatabaseAPI.getResponses(MainActivity.this)));
    }
}
