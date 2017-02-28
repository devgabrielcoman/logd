/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Answer;
import com.gabrielcoman.logd.models.Question;
import com.gabrielcoman.logd.models.Response;
import com.gabrielcoman.logd.rxdatasource.RxDataSource;
import com.gabrielcoman.logd.system.database.DatabaseManager;

import java.util.List;

public class AnswerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String questionStr = bundle.getString("question");
            Question question = new Question(questionStr);

            ListView listView = (ListView) findViewById(R.id.AnswersList);

            List<Answer> possibleAnswers = question.getPossibleAnswers();

            RxDataSource.from(AnswerActivity.this, possibleAnswers)
                    .bindTo(listView)
                    .customiseRow(R.layout.row_answer, Answer.class, (answer, view) -> {

                        TextView answerText = (TextView) view.findViewById(R.id.AnswerText);
                        answerText.setText(answer.getTitle());

                    })
                    .onRowClick(R.layout.row_answer, (index, answer) -> {

                        if (index < possibleAnswers.size() - 1) {

                            Response response = new Response(answer);

                            DatabaseManager.writeToDatabase(AnswerActivity.this, response);

                            Intent mainIntent = new Intent(AnswerActivity.this, MainActivity.class);
                            AnswerActivity.this.startActivity(mainIntent);

                        } else {
                            Intent mainIntent = new Intent(AnswerActivity.this, JournalActivity.class);
                            AnswerActivity.this.startActivity(mainIntent);
                        }
                    })
                    .update();
        }
    }
}
