/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.answer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.api.DatabaseAPI;
import com.gabrielcoman.logd.system.api.SentimentAPI;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import gabrielcoman.com.rxdatasource.RxDataSource;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;

public class AnswerActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 112;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);


        TextView questionText = (TextView) findViewById(R.id.AnswersQuestionText);
        ListView listView = (ListView) findViewById(R.id.AnswersList);
        // Button journalButton = (Button) findViewById(R.id.JournalButton);

//        RxView.clicks(journalButton)
//                .subscribe(aVoid -> {
//
//                    Intent journalIntent = new Intent(AnswerActivity.this, JournalActivity.class);
//                    AnswerActivity.this.startActivityForResult(journalIntent, SET_REQ_CODE);
//
//                });

        getStringExtras("question")
                .map(s -> new Gson().fromJson(s, Question.class))
                .subscribe(question -> {

                    // set title
                    questionText.setText(question.getTitle());

                    // set answers
                    Observable.from(question.getAnswers())
                            .map(ChooseAnswerViewModel::new)
                            .toList()
//                            .map(chooseAnswerViewModels -> {
//                                List<Object> result = new ArrayList<>();
//                                result.addAll(chooseAnswerViewModels);
//                                result.add(new ChoseJournalViewModel(AnswerActivity.this));
//                                return result;
//                            })
                            .subscribe(models -> {

                                RxDataSource.create(AnswerActivity.this)
                                        .bindTo(listView)
                                        .customiseRow(R.layout.row_answer, ChooseAnswerViewModel.class, (view, model) -> {

                                            ((TextView) view.findViewById(R.id.AnswersText)).setText(model.getTitle());

                                        })
//                                        .customiseRow(R.layout.row_journal, ChoseJournalViewModel.class, (view, model) -> {
//
//                                            ((TextView) view.findViewById(R.id.JournalAnswersText)).setText(model.getTitle());
//
//                                        })
                                        .onRowClick(R.layout.row_answer, (Action2<Integer, ChooseAnswerViewModel>) (pos, model) -> {

                                            SentimentAPI
                                                    .analyseSentiment(model.getTitle())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(value -> {
                                                        Response response = new Response(model.getTitle(), value);
                                                        DatabaseAPI.writeResponse(AnswerActivity.this, response);
                                                        finishOK();
                                                    });
                                        })
//                                        .onRowClick(R.layout.row_journal, pos -> {
//
//                                            Intent journalIntent = new Intent(AnswerActivity.this, JournalActivity.class);
//                                            AnswerActivity.this.startActivityForResult(journalIntent, SET_REQ_CODE);
//                                        })
                                        .update(models);
                            });

                });

        setOnActivityResult(this::finishOK);
    }
}
