/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.api.SentimentAPI;
import com.google.gson.Gson;

import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.functions.Action0;

public class AnswerActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 112;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String questionStr = bundle.getString("question");
            Question question = new Gson().fromJson(questionStr, Question.class);

            ListView listView = (ListView) findViewById(R.id.AnswersList);

            List<String> possibleAnswers = question.getAnswers();

            // add the "write to journal" option
            possibleAnswers.add(getString(R.string.data_question_general_answer_journal));

            RxDataSource.from(AnswerActivity.this, possibleAnswers)
                    .bindTo(listView)
                    .customiseRow(R.layout.row_answer, String.class, (answer, view) -> {

                        TextView answerText = (TextView) view.findViewById(R.id.AnswerText);
                        answerText.setText(answer);

                    })
                    .onRowClick(R.layout.row_answer, (index, answer) -> {

                        if (index < possibleAnswers.size() - 1) {

                            SentimentAPI
                                    .analyseSentiment(answer)
                                    .subscribe(value -> {
                                        Response response = new Response(answer, value);
                                        DatabaseAPI.writeResponse(AnswerActivity.this, response);
                                        finishOK();
                                    });

                        } else {

                            Intent journalIntent = new Intent(AnswerActivity.this, JournalActivity.class);
                            AnswerActivity.this.startActivityForResult(journalIntent, SET_REQ_CODE);
                        }
                    })
                    .update();

            setOnActivityResult(this::finishOK);
        }
    }
}
