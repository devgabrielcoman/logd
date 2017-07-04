/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.journal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Profile;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.library.network.AddResponseRequest;
import com.gabrielcoman.logd.library.network.GetSentimentRequest;
import com.gabrielcoman.logd.library.network.NetworkTask;
import com.gabrielcoman.logd.library.parse.ParseRequest;
import com.gabrielcoman.logd.library.parse.ParseSentimentTask;
import com.gabrielcoman.logd.models.Response;
import com.jakewharton.rxbinding.view.RxView;

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
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Button save = (Button) findViewById(R.id.JournalSave);
        EditText journalText = (EditText) findViewById(R.id.JournalText);

        RxView.clicks(save)
                .map(aVoid -> journalText.getText().toString())
                .toSingle()
                .flatMap(answer -> {
                    GetSentimentRequest request = new GetSentimentRequest(answer);
                    NetworkTask<GetSentimentRequest> task = new NetworkTask<>();
                    return task.execute(request);
                })
                .flatMap(payload -> {
                    ParseRequest request1 = new ParseRequest(payload);
                    ParseSentimentTask task1 = new ParseSentimentTask();
                    return task1.execute(request1);
                })
                .map(sentiment -> new Response(journalText.getText().toString(), sentiment))
                .flatMap(response -> {
                    Profile profile = Profile.getCurrentProfile();
                    String id = profile.getId();
                    AddResponseRequest request1 = new AddResponseRequest(id, response);
                    NetworkTask<AddResponseRequest> task1 = new NetworkTask<>();
                    return task1.execute(request1);
                })
                .subscribe(result -> {
                    Toast.makeText(JournalActivity.this, R.string.data_question_answered_toast, Toast.LENGTH_SHORT).show();
                    finishOK();
                }, throwable -> {
                    finishOK();
                });
    }
}
