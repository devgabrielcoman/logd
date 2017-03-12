/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.journal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.api.SentimentAPI;
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
                .subscribe(aVoid -> {

                    final String text = journalText.getText().toString();

                    SentimentAPI.analyseSentiment(text)
                            .subscribe(sentiment -> {

                                Response response = new Response(text, sentiment);
                                DatabaseAPI.writeResponse(JournalActivity.this, response);
                                finishOK();

                            });

                });
    }
}
