/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.answer.AnswerActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.aux.LogdAux;
import com.gabrielcoman.logd.system.setup.AppSetup;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.subjects.PublishSubject;

public class MainActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.MainToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        AppSetup.setupAlarmsOnFirstOpen(this);

        FloatingActionButton journalButton = (FloatingActionButton) findViewById(R.id.JournalButton);

        ListView history = (ListView) findViewById(R.id.History);
        ViewCompat.setNestedScrollingEnabled(history, true);

        LogdLineChart lineChartView = (LogdLineChart) findViewById(R.id.Chart);

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
                    return viewModels;
                })
                .map(models -> {

                    HashMap<Long, List<ResponseViewModel>> map = new HashMap<>();
                    for (ResponseViewModel vm : models) {
                        if (!map.containsKey(vm.getDayTimestamp())) {
                            List<ResponseViewModel> list = new ArrayList<>();
                            list.add(vm);
                            map.put(vm.getDayTimestamp(), list);
                        } else {
                            map.get(vm.getDayTimestamp()).add(vm);
                        }
                    }

                    List<ResponseGroupViewModel> finalList = new ArrayList<>();
                    for (Long key : map.keySet()) {
                        finalList.add(new ResponseGroupViewModel(key, map.get(key)));
                    }
                    Collections.sort(finalList);

                    return finalList;
                })
                .subscribe(models -> {

                    int length = models.size() >= 7 ? 7 : models.size();
                    String [] labels = new String[length];
                    float[] values = new float[length];
                    for (int i = length - 1; i >= 0; i--) {
                        int t = (length - 1) - i;
                        labels[t] = models.get(i).getDayAndMonth();
                        values[t] = (float) models.get(i).getAverage();
                    }

                    lineChartView.setData(labels, values);

                    RxDataSource.create(MainActivity.this)
                            .bindTo(history)
                            .customiseRow(R.layout.row_history, ResponseGroupViewModel.class, (view, model) -> {

                                ((TextView) view.findViewById(R.id.DayOfMonth)).setText(model.getDayOfMonth());
                                ((TextView) view.findViewById(R.id.MonthYear)).setText(model.getMonthYear());

                                LinearLayout holder = (LinearLayout) view.findViewById(R.id.ResponseHolder);

                                for (ResponseViewModel vm : model.getViewModels()) {
                                    TextView date = new TextView(MainActivity.this);
                                    date.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    date.setText(vm.getHour());
                                    date.setTextColor(getResources().getColor(R.color.colorLightAccent));
                                    holder.addView(date);

                                    TextView content = new TextView(MainActivity.this);
                                    content.setPadding(0, 0, (int)LogdAux.dipToPixels(MainActivity.this, 12), (int)LogdAux.dipToPixels(MainActivity.this, 12));
                                    content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    content.setTextColor(getResources().getColor(R.color.colorTextMain2));
                                    content.setText(vm.getAnswer());
                                    holder.addView(content);
                                }

                            })
                            .update(models);

                });

        setOnActivityResult(() -> subject.onNext(DatabaseAPI.getResponses(MainActivity.this)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
