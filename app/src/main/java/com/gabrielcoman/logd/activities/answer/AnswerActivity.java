/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.answer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.journal.JournalActivity;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.system.api.SentimentAPI;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;

import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;

public class AnswerActivity extends BaseActivity {

    private static final int SET_REQ_CODE = 112;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        TextView questionText = (TextView) findViewById(R.id.QuestionTitle);
        Button answerText1 = (Button) findViewById(R.id.NotificationAnswerText1);
        Button answerText2 = (Button) findViewById(R.id.NotificationAnswerText2);
        Button answerText3 = (Button) findViewById(R.id.NotificationAnswerText3);
        Button journal = (Button) findViewById(R.id.JournalButton);

        Single<String> questionRx = getStringExtras("question");
        Single<Boolean> isMorningRx = getBooleanExtras("isMorning");

        Observable.combineLatest(questionRx.toObservable(), isMorningRx.toObservable(), AnswerActivityExtra::new)
                .subscribe(extra -> {

                    Question question = extra.getQuestion();

                    questionText.setText(question.getTitle());
                    answerText1.setText(question.getAnswers().get(0));
                    answerText2.setText(question.getAnswers().get(1));
                    answerText3.setText(question.getAnswers().get(2));

                    RxView.clicks(answerText1)
                            .subscribe(aVoid -> analyseSentiment(question.getAnswers().get(0), extra.isMorning()));

                    RxView.clicks(answerText2)
                            .subscribe(aVoid -> analyseSentiment(question.getAnswers().get(1), extra.isMorning()));

                    RxView.clicks(answerText3)
                            .subscribe(aVoid -> analyseSentiment(question.getAnswers().get(2), extra.isMorning()));

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
    private Question question;
    private boolean isMorning;

    AnswerActivityExtra(String q, boolean isMorning) {
        this.isMorning = isMorning;
        question = new Gson().fromJson(q, Question.class);
    }

    Question getQuestion() {
        return question;
    }

    boolean isMorning() {
        return isMorning;
    }
}
