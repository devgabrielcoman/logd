/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.journal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.Profile;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.library.network.AddResponseRequest;
import com.gabrielcoman.logd.library.network.GetSentimentRequest;
import com.gabrielcoman.logd.library.network.NetworkTask;
import com.gabrielcoman.logd.library.parse.ParseRequest;
import com.gabrielcoman.logd.library.parse.ParseSentimentTask;
import com.gabrielcoman.logd.library.profile.GetProfileRequest;
import com.gabrielcoman.logd.library.profile.GetProfileTask;
import com.gabrielcoman.logd.models.Response;
import com.jakewharton.rxbinding.view.RxView;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class JournalActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.JournalToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Button save = (Button) findViewById(R.id.JournalSave);
        ProgressBar spinner = (ProgressBar) findViewById(R.id.Spinner);
        EditText journalText = (EditText) findViewById(R.id.JournalText);

        RxView.clicks(save)
                .doOnNext(aVoid -> spinner.setVisibility(View.VISIBLE))
                .map(aVoid -> journalText.getText().toString())
                .subscribe(answer -> {

                    GetSentimentRequest request = new GetSentimentRequest(answer);
                    NetworkTask<GetSentimentRequest> task = new NetworkTask<>();
                    task.execute(request)
                            .flatMap(payload -> {
                                ParseRequest request1 = new ParseRequest(payload);
                                ParseSentimentTask task1 = new ParseSentimentTask();
                                return task1.execute(request1);
                            })
                            .map(sentiment -> new Response(answer, sentiment))
                            .flatMap(response -> {

                                GetProfileRequest request1 = new GetProfileRequest();
                                GetProfileTask task1 = new GetProfileTask();

                                return Single.zip(Single.just(response), task1.execute(request1), (response1, profile) -> new AddResponseRequest(profile.getId(), response1));
                            })
                            .flatMap(addResponseRequest -> {
                                NetworkTask<AddResponseRequest> task1 = new NetworkTask<>();
                                return task1.execute(addResponseRequest);
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSuccess(s -> spinner.setVisibility(View.GONE))
                            .doOnError(throwable -> spinner.setVisibility(View.GONE))
                            .subscribe(result -> {
                                Toast.makeText(JournalActivity.this, R.string.data_question_answered_toast, Toast.LENGTH_SHORT).show();
                                finishOK();
                            }, throwable -> {
                                Toast.makeText(JournalActivity.this, R.string.data_question_answered_toast_error, Toast.LENGTH_LONG).show();
                            });
                });
    }
}
