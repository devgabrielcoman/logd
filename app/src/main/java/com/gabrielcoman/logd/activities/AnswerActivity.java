/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Answer;
import com.gabrielcoman.logd.models.Question;

import gabrielcoman.com.rxdatasource.RxDataSource;

public class AnswerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String qString = bundle.getString("question");
            Question question = new Question(qString);

            ListView listView = (ListView) findViewById(R.id.AnswersList);

            RxDataSource.from(AnswerActivity.this, question.getPossibleAnswers())
                    .bindTo(listView)
                    .customiseRow(R.layout.row_answer, Answer.class, (answer, view) -> {

                        TextView answerText = (TextView) view.findViewById(R.id.AnswerText);
                        answerText.setText(answer.getName());

                    })
                    .onRowClick(R.layout.row_answer, (index, answer) -> {

                        // clicked on
                        Log.d("Logd-App", "Selected " + answer.getName() + " | " + answer.getValue());

                    })
                    .update();
        }
    }
}
