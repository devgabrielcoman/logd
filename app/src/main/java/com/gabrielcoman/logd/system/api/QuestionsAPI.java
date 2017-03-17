package com.gabrielcoman.logd.system.api;

import android.content.Context;
import android.util.Log;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Question;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;

public class QuestionsAPI {

    private static String readFromFile (Context context, int ID) {
        InputStream is = context.getResources().openRawResource(ID);
        try {
            return new String(ByteStreams.toByteArray(is));
        } catch (IOException e) {
            return "[]";
        }
    }

    private static Single<Question> getQuestion (Context context, int title, int file) {

        String json = readFromFile(context, file);
        List<String> allAnswers = new ArrayList<>();

        try {
            allAnswers = Arrays.asList(new Gson().fromJson(json, String[].class));
        } catch (JsonSyntaxException e) {
            // do nothing
        }

        Collections.shuffle(allAnswers, new Random());

        List<String> selectedAnswers = new ArrayList<>();

        int max = allAnswers.size() > 3 ? 3 : allAnswers.size();
        for (int i = 0; i < max; i++) {
            selectedAnswers.add(allAnswers.get(i));
        }

        return Single.create(subscriber -> {

            Question q = new Question(context.getString(title), selectedAnswers);
            subscriber.onSuccess(q);

        });

    }

    public static Single<Question> getMorningQuestion (Context context) {
        return getQuestion(context, R.string.data_question_morning_title, R.raw.answers_morning);
    }

    public static Single<Question> getEveningQuestion (Context context) {
        return getQuestion(context, R.string.data_question_evening_title, R.raw.answers_evening);
    }

}
