package com.gabrielcoman.logd.library.parse;

import android.util.Log;

import com.gabrielcoman.logd.library.Task;
import com.gabrielcoman.logd.models.Question;
import com.google.gson.Gson;

import rx.Single;

public class ParseAnswersTask implements Task <ParseRequest, Question> {

    @Override
    public Single<Question> execute(ParseRequest input) {
        return Single.create(subscriber -> {
                try {
                Gson gson = new Gson();
                Question question = gson.fromJson(input.contents, Question.class);
                subscriber.onSuccess(question);
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
