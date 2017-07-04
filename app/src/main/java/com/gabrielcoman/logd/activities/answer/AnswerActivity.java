/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.answer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.library.network.GetAnswersRequest;
import com.gabrielcoman.logd.library.network.NetworkTask;
import com.gabrielcoman.logd.library.parse.ParseAnswersTask;
import com.gabrielcoman.logd.library.parse.ParseRequest;
import com.jakewharton.rxbinding.view.RxView;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;

public class AnswerActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 112;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        TextView questionText = (TextView) findViewById(R.id.QuestionTitle);
        ListView answers = (ListView) findViewById(R.id.AnswersList);
        Button journal = (Button) findViewById(R.id.JournalButton);

        Single<String> questionRx = getStringExtras("question");
        Single<Boolean> isMorningRx = getBooleanExtras("isMorning");

        Single.zip(questionRx, isMorningRx, AnswerActivityExtra::new)
                .flatMap(extra -> {
                    GetAnswersRequest request = new GetAnswersRequest(extra.getQuestion(), extra.isMorning());
                    NetworkTask<GetAnswersRequest> task = new NetworkTask<>();
                    return task.execute(request);
                })
                .flatMap(payload -> {
                    ParseRequest request = new ParseRequest(payload);
                    ParseAnswersTask task = new ParseAnswersTask();
                    return task.execute(request);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(question -> {

                    questionText.setText(question.getQuestion());

                    RxDataSource.create(AnswerActivity.this)
                            .bindTo(answers)
                            .customiseRow(R.layout.row_answer, String.class, (view, answer) -> {
                                ((TextView) view.findViewById(R.id.AnswerText)).setText(answer);
                            })
                            .onRowClick(R.layout.row_answer, (Action2<Integer, String>) (integer, s) -> {
                                // do nothing
                            })
                            .update(question.getAnswers());

                }, throwable -> {
                    Log.e("Logd", throwable.getMessage());
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
