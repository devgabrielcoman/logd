package com.gabrielcoman.logd.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Answer;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.database.DatabaseManager;
import com.gabrielcoman.logd.system.network.SentimentAnalysis;
import com.jakewharton.rxbinding.view.RxView;

public class JournalActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        Button next = (Button) findViewById(R.id.JournalOK);
        EditText journalText = (EditText) findViewById(R.id.JournalText);

        RxView.clicks(next)
                .subscribe(aVoid -> {

                    final String text = journalText.getText().toString();

                    SentimentAnalysis.analyseSentiment(text)
                            .subscribe(sentiment -> {

                                Answer answer = Answer.responseAnswer(text, sentiment);
                                Response response = new Response(answer);
                                DatabaseManager.writeToDatabase(JournalActivity.this, response);

                                Intent mainIntent = new Intent(JournalActivity.this, MainActivity.class);
                                JournalActivity.this.startActivity(mainIntent);

                            });

                });
    }
}