/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.answer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.network.GetAnswers;
import com.google.gson.Gson;
import com.google.gson.annotations.Since;
import com.jakewharton.rxbinding.view.RxView;

import java.io.IOException;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;

public class AnswerActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 112;

    private RxDataSource dataSource = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        TextView questionText = (TextView) findViewById(R.id.QuestionTitle);
        ListView answers = (ListView) findViewById(R.id.AnswersList);
        Button journal = (Button) findViewById(R.id.JournalButton);

        Single<String> questionRx = getStringExtras("question");
        Single<Boolean> isMorningRx = getBooleanExtras("isMorning");

        dataSource = RxDataSource.create(AnswerActivity.this);
        dataSource
                .bindTo(answers)
                .customiseRow(R.layout.row_answer, String.class, (view, answer) -> {

                    Log.d("Logd", "Answer: " + answer);

                    ((TextView) view.findViewById(R.id.AnswerText)).setText(answer);
                });

        GetAnswers getAnswers = new GetAnswers();

        Observable.combineLatest(questionRx.toObservable(), isMorningRx.toObservable(), AnswerActivityExtra::new)
                .flatMap(extra -> getAnswers.execute(extra.isMorning(), extra.getQuestion()).toObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    try {
                        String resp = response.body().string();
                        Gson gson = new Gson();
                        Question question = gson.fromJson(resp, Question.class);

                        questionText.setText(question.getQuestion());

                        dataSource.update(question.getAnswers());

                    } catch (IOException e) {
                        Log.d("Logd", e.getMessage());
                    }
                }, throwable -> {
                    Log.d("Logd-Throwable", throwable.getMessage());
                });

        RxView.clicks(journal)
                .subscribe(aVoid -> {

                    Intent journalIntent = new Intent(AnswerActivity.this, JournalActivity.class);
                    AnswerActivity.this.startActivityForResult(journalIntent, SET_REQ_CODE);

                });

        setOnActivityResult(this::finishOK);
    }

    private void analyseSentiment (String answer, boolean isMorning) {
//        SentimentAPI
//                .analyseSentiment(answer)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(value -> {
//                    Response response = new Response(answer, value);
//                    if (isMorning) {
//                        DatabaseAPI.writeMorningResponse(AnswerActivity.this, response);
//                    } else {
//                        DatabaseAPI.writeEveningResponse(AnswerActivity.this, response);
//                    }
//                    Toast.makeText(AnswerActivity.this, R.string.data_question_answered_toast, Toast.LENGTH_SHORT).show();
//                    finishOK();
//                });
    }
}

class AnswerActivityExtra {
    private String question;
    private boolean isMorning;

    AnswerActivityExtra(String q, boolean isMorning) {
        this.isMorning = isMorning;
        this.question = q;
    }

    String  getQuestion() {
        return question;
    }

    boolean isMorning() {
        return isMorning;
    }
}
