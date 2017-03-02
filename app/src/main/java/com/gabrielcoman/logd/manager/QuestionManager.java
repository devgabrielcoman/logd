/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.manager;

import android.content.Context;

import com.gabrielcoman.logd.models.Question;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;

public class QuestionManager {

    public static Observable<Question> getPossibleQuestions (Context context) {

        List<Question> questions = Arrays.asList(
                Question.morningQuestion1(context),
                Question.morningQuestion2(context),
                Question.morningQuestion3(context),
                Question.eveningQuestion1(context),
                Question.eveningQuestion2(context),
                Question.eveningQuestion3(context),
                Question.eveningQuestion4(context)
        );

        return Observable.create(subscriber -> {

            for (Question q : questions) {
                subscriber.onNext(q);
            }
            subscriber.onCompleted();

        });
    }

    public static Question pickFromList (List<Question> questions) {
        int index = randomNumberBetween(0, questions.size() - 1);
        return questions.get(index);
    }

    private static int randomNumberBetween(int min, int max){
        Random rand  = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

}
