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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.activities.setup.SetupActivity;
import com.gabrielcoman.logd.aux.Aux;
import com.gabrielcoman.logd.library.network.GetResponsesRequest;
import com.gabrielcoman.logd.library.network.NetworkTask;
import com.gabrielcoman.logd.library.parse.ParseRequest;
import com.gabrielcoman.logd.library.parse.ParseResponsesTask;
import com.gabrielcoman.logd.models.Response;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class MainActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton journalButton = (FloatingActionButton) findViewById(R.id.JournalButton);
        ListView history = (ListView) findViewById(R.id.History);
        ImageView profilePicture = (ImageView) findViewById(R.id.ProfilePicture);
        TextView profileName = (TextView) findViewById(R.id.ProfileName);
        TextView profileNoLogs = (TextView) findViewById(R.id.ProfileNrLogs);

        Profile profile = Profile.getCurrentProfile();
        String id = profile.getId();

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
                    collapsingToolbarLayout.setTitle(profile.getName());
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        ViewCompat.setNestedScrollingEnabled(history, true);

        //
        // setup picture
        Picasso.with(MainActivity.this)
                .load(profile.getProfilePictureUri(260, 260))
                .into(profilePicture);

        //
        // setup profile
        profileName.setText(profile.getName());

        GetResponsesRequest request = new GetResponsesRequest(id);
        NetworkTask<GetResponsesRequest> task = new NetworkTask<>();
        task.execute(request)
                .toObservable()
                .flatMap(rawData -> {
                    ParseRequest request1 = new ParseRequest(rawData);
                    ParseResponsesTask task1 = new ParseResponsesTask();
                    return task1.execute(request1);
                })
                .map(ResponseViewModel::new)
                .toList()
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
                .map(models -> {
                    List<Object> finalModels = new ArrayList<>();

                    ChartViewModel chartViewModel = new ChartViewModel(models);
                    finalModels.add(chartViewModel);

                    for (ResponseGroupViewModel vm : models) {
                        finalModels.add(vm);
                    }

                    return finalModels;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {

                    //
                    // customise the table
                    RxDataSource.create(MainActivity.this)
                            .bindTo(history)
                            .customiseRow(R.layout.row_graph, ChartViewModel.class, (view, model) -> {

                                LogdLineChart logdLineChart = (LogdLineChart) view.findViewById(R.id.Chart);
                                logdLineChart.setData(model.getLabels(), model.getValues());

                            })
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
                                    content.setPadding(0, 0, (int)Aux.dipToPixels(MainActivity.this, 12), (int)Aux.dipToPixels(MainActivity.this, 12));
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
}
