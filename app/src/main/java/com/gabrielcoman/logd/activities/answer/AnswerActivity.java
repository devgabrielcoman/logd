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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.library.network.AddResponseRequest;
import com.gabrielcoman.logd.library.network.GetAnswersRequest;
import com.gabrielcoman.logd.library.network.GetSentimentRequest;
import com.gabrielcoman.logd.library.network.NetworkTask;
import com.gabrielcoman.logd.library.parse.ParseAnswersTask;
import com.gabrielcoman.logd.library.parse.ParseRequest;
import com.gabrielcoman.logd.library.parse.ParseSentimentTask;
import com.gabrielcoman.logd.library.profile.GetProfileRequest;
import com.gabrielcoman.logd.library.profile.GetProfileTask;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.jakewharton.rxbinding.view.RxView;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
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
        ProgressBar spinner = (ProgressBar) findViewById(R.id.Spinner);

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
                .doOnSuccess(question -> spinner.setVisibility(View.GONE))
                .subscribe(question -> {

                    questionText.setText(question.getQuestion());

                    RxDataSource.create(AnswerActivity.this)
                            .bindTo(answers)
                            .customiseRow(R.layout.row_answer, String.class, (view, answer) -> {
                                ((TextView) view.findViewById(R.id.AnswerText)).setText(answer);
                            })
                            .onRowClick(R.layout.row_answer, (Action2<Integer, String>) (integer, answer) -> {

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
                                        .doOnSubscribe(() -> spinner.setVisibility(View.VISIBLE))
                                        .doOnError(throwable -> spinner.setVisibility(View.GONE))
                                        .subscribe(result -> {
                                            Toast.makeText(AnswerActivity.this, R.string.data_question_answered_toast, Toast.LENGTH_SHORT).show();
                                            finishOK();
                                        }, throwable -> {
                                            Toast.makeText(AnswerActivity.this, R.string.data_question_answered_toast_error, Toast.LENGTH_LONG).show();
                                        });
                            })
                            .update(question.getAnswers());

                }, throwable -> spinner.setVisibility(View.GONE));

        RxView.clicks(journal)
                .subscribe(aVoid -> {

                    Intent journalIntent = new Intent(AnswerActivity.this, JournalActivity.class);
                    AnswerActivity.this.startActivityForResult(journalIntent, SET_REQ_CODE);

                });

        setOnActivityResult(this::finishOK);
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
