/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.library.network.GetResponsesRequest;
import com.gabrielcoman.logd.library.network.NetworkTask;
import com.gabrielcoman.logd.library.parse.ParseRequest;
import com.gabrielcoman.logd.library.parse.ParseResponsesTask;
import com.gabrielcoman.logd.models.Response;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogdLineChart lineChartView = (LogdLineChart) findViewById(R.id.Chart);
        FloatingActionButton journalButton = (FloatingActionButton) findViewById(R.id.JournalButton);
        ListView history = (ListView) findViewById(R.id.History);

        //
        // @// TODO: 05/07/2017 See if this will remain the same
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.MainToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.CollapsingToolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.AppBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        ViewCompat.setNestedScrollingEnabled(history, true);

        Profile profile = Profile.getCurrentProfile();
        String id = profile.getId();
        GetResponsesRequest request = new GetResponsesRequest(id);
        NetworkTask<GetResponsesRequest> task = new NetworkTask<>();
        task.execute(request)
                .flatMap(responses -> {
                    ParseRequest request1 = new ParseRequest(responses);
                    ParseResponsesTask task1 = new ParseResponsesTask();
                    return task1.execute(request1);
                })
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {

                    if (models.size() > 0) {
                        int length = models.size() >= 7 ? 7 : models.size();
                        String[] labels = new String[length];
                        float[] values = new float[length];
                        for (int i = length - 1; i >= 0; i--) {
                            int t = (length - 1) - i;
                            labels[t] = models.get(i).getDayAndMonth();
                            values[t] = (float) models.get(i).getAverage();
                        }

                        lineChartView.setData(labels, values);
                    }

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
                                    date.setTextColor(getResources().getColor(R.color.primary));
                                    holder.addView(date);

                                    TextView content = new TextView(MainActivity.this);
                                    content.setPadding(0, 0, (int)dipToPixels(MainActivity.this, 12), (int)dipToPixels(MainActivity.this, 12));
                                    content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    content.setTextColor(getResources().getColor(R.color.secondary_text));
                                    content.setText(vm.getAnswer());
                                    holder.addView(content);
                                }

                            })
                            .update(models);

                }, throwable -> {
                    Log.e("Logd", throwable.getMessage());
                });

        RxView.clicks(journalButton)
                .subscribe(aVoid -> {
                    Intent journalIntent = new Intent(MainActivity.this, JournalActivity.class);
                    MainActivity.this.startActivityForResult(journalIntent, SET_REQ_CODE);
                });
    }

    public float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
