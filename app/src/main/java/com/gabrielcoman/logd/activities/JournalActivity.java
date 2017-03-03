package com.gabrielcoman.logd.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.api.SentimentAPI;
import com.jakewharton.rxbinding.view.RxView;

public class JournalActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        Button next = (Button) findViewById(R.id.JournalOK);
        EditText journalText = (EditText) findViewById(R.id.JournalText);

        RxView.clicks(next)
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
