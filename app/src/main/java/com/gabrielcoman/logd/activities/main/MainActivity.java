/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.answer.AnswerActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.alarm.AlarmScheduler;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.setup.AppSetup;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppSetup.setupAlarmsOnFirstOpen(this);

        FloatingActionButton journalButton = (FloatingActionButton) findViewById(R.id.JournalButton);

        ListView history = (ListView) findViewById(R.id.History);

        getStringExtras("question")
                .subscribe(s -> {
                    Intent mainIntent = new Intent(MainActivity.this, AnswerActivity.class);
                    mainIntent.putExtra("question", s);
                    startActivityForResult(mainIntent, SET_REQ_CODE);
                });

        RxView.clicks(journalButton)
                .subscribe(aVoid -> {
                    Intent journalIntent = new Intent(MainActivity.this, JournalActivity.class);
                    MainActivity.this.startActivityForResult(journalIntent, SET_REQ_CODE);
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
                            .customiseRow(R.layout.row_history, ResponseViewModel.class, (view, model) -> {

                                ((TextView) view.findViewById(R.id.DayOfMonth)).setText(model.getDayOfMonth());
                                ((TextView) view.findViewById(R.id.MonthYear)).setText(model.getMonthYear());
                                ((TextView) view.findViewById(R.id.AnwerText)).setText(model.getAnswer());

//                                ((TextView) view.findViewById(R.id.ResponseSentiment)).setText(model.getValue());
//                                ((TextView) view.findViewById(R.id.ResponseDate)).setText(model.getDate());
//                                ((TextView) view.findViewById(R.id.ResponseText)).setText(model.getAnswer());

                            })
                            .update(models);

                });

        setOnActivityResult(() -> subject.onNext(DatabaseAPI.getResponses(MainActivity.this)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ActionTrigger) {
            AppSetup.scheduleTestAlarm(MainActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
