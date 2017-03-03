package com.gabrielcoman.logd.system.api;

import android.content.Context;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.models.Question;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import rx.Observable;

public class QuestionsAPI {

    private static String readFromFile (Context context, int ID) {
        InputStream is = context.getResources().openRawResource(ID);
        try {
            return new String(ByteStreams.toByteArray(is));
        } catch (IOException e) {
            return "[]";
        }
    }

    public static Observable<Question> getPossibleQuestions (Context context) {

        String json = readFromFile(context, R.raw.questions);
        List<Question> list = new ArrayList<>();

        try {
            list = Arrays.asList(new Gson().fromJson(json, Question[].class));
        } catch (JsonSyntaxException e) {
            // do nothing
        }

        return Observable.from(list);
    }

    public static Question pickFromList (List<Question> list) {
        int index = randomNumberBetween(0, list.size() - 1);
        return list.get(index);
    }

    private static int randomNumberBetween(int min, int max){
        Random rand  = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

}
