/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.answer.AnswerActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.alarm.AlarmScheduler;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.aux.LogdAux;
import com.gabrielcoman.logddatabase.Database;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import tv.superawesome.lib.sautils.SAAlert;
import tv.superawesome.lib.sautils.SAAlertInterface;

public class MainActivity extends BaseActivity {

    private Menu  menu;
    private PublishSubject<List<Response>> subject;

    private static final int SET_REQ_CODE = 111;

    private static final int REQUEST = 1;
    private static final String PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;

    private boolean firstStart = true;
    private boolean granted = false;
    private boolean alert = false;
    private boolean neveragain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        subject = PublishSubject.create();

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
                                    content.setPadding(0, 0, (int)LogdAux.dipToPixels(MainActivity.this, 12), (int)LogdAux.dipToPixels(MainActivity.this, 12));
                                    content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    content.setTextColor(getResources().getColor(R.color.secondary_text));
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
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_main, menu);

        boolean areSet = AlarmScheduler.areAlarmsSet(this);

        if (areSet) {
            menu.getItem(0).setTitle("Stop daily questions");
        } else {
            menu.getItem(0).setTitle("Set daily questions");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ActionTrigger) {

            boolean areSet = AlarmScheduler.areAlarmsSet(this);

            if (areSet) {
                setAlarmsOff();
            } else {
                setAlarmsOn();
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (subject != null) {
            subject.onNext(DatabaseAPI.getResponses(MainActivity.this));
        }

        updateState();
    }

    private void updateState () {
        granted = ActivityCompat.checkSelfPermission(this, PERMISSION) == GRANTED;
        neveragain = DatabaseAPI.shouldNeverShowPermissions(this);
        firstStart = DatabaseAPI.firstStart(this);

        if (!granted && !firstStart) {
            Log.d("Logd-App", "At resume there are no permissions so unscheduled alarms");
            setAlarmsAndMenuOff();
        }
        alert = AlarmScheduler.areAlarmsSet(this);

        if (firstStart) {
            Log.d("Logd-App", "At resume found out it's first start so asking for permissions");
            DatabaseAPI.writeAppStarted(this);
            setAlarmsOn();
        }
    }

    private void setAlarmsOn () {

        //
        // when trying to set alarms, we've discovered we don't have any permissions
        if (!granted) {

            //
            // if the user hasn't yet selected he doesn't want the permission pop-up to show
            // we just show him the popup
            if (!neveragain) {

                SAAlert.getInstance()
                        .show(this,
                                getString(R.string.permission_first_title),
                                getString(R.string.permission_first_message),
                                getString(R.string.permission_first_ok),
                                getString(R.string.permission_first_nok),
                                false,
                                0,
                                (i, s) -> {
                                    // first button
                                    if (i == 0) {
                                        ActivityCompat.requestPermissions(this, new String[]{PERMISSION}, REQUEST);
                                    }
                                });


            }
            //
            // if the user has said he doesn't want the app asking for permissions
            // just send him to settings
            else {

                SAAlert.getInstance()
                        .show(this,
                                getString(R.string.permission_settings_title),
                                getString(R.string.permission_settings_message),
                                getString(R.string.permission_settings_ok),
                                getString(R.string.permission_settings_nok),
                                false,
                                0,
                                (i, s) -> {
                                    // first button
                                    if (i == 0) {
                                        gotoSettings(this);
                                    }
                                });

            }
        }
        //
        // we have the required permissions
        else {
            setAlarmsAndMenuOn();
        }
    }

    private void setAlarmsOff () {
        setAlarmsAndMenuOff();
    }

    private void setAlarmsAndMenuOn () {

        AlarmScheduler.scheduleAlarms(this)
                .subscribe(success -> {

                    if (success) {
                        DatabaseAPI.setAlarmStatus(MainActivity.this, true);

                        if (menu != null && menu.getItem(0) != null) {
                            menu.getItem(0).setTitle(getResources().getString(R.string.data_question_questions_stop_title));
                        }

                        Toast.makeText(MainActivity.this, R.string.data_question_questions_set, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("Logd-App", "Could not schedule alarms!");
                    }

                });
    }

    private void setAlarmsAndMenuOff () {

        AlarmScheduler.unscheduleAlarms(this)
                .subscribe(success -> {

                    if (success) {

                        DatabaseAPI.setAlarmStatus(MainActivity.this, false);

                        if (menu != null && menu.getItem(0) != null) {
                            menu.getItem(0).setTitle(getResources().getString(R.string.data_question_questions_set_title));
                        }

                        Toast.makeText(MainActivity.this, R.string.data_question_questions_stopped, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Log.d("Logd-App", "Could not unschedule alarms!");
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST: {

                if(ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)){
                    //denied
                    Log.e("Logd-App", "Permission denied!");
                    setAlarmsAndMenuOff();
                }else{
                    if(ActivityCompat.checkSelfPermission(this, PERMISSION) == GRANTED){
                        setAlarmsAndMenuOn();
                    } else {
                        DatabaseAPI.writeNeverShowPermissions(this);
                        neveragain = true;
                    }
                }
            }
        }
    }

    private static void gotoSettings (final Activity context) {
        if (context != null) {
            final Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + context.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(i);
        }
    }
}
