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

public class QuestionManager {

    public static Question getNewQuestion (Context context, Date date) {

        List<Question> possibleQuestions = Arrays.asList(
                Question.morningQuestion1(context),
                Question.morningQuestion2(context),
                Question.morningQuestion3(context),
                Question.eveningQuestion1(context),
                Question.eveningQuestion2(context),
                Question.eveningQuestion3(context),
                Question.eveningQuestion4(context)
        );

        int question = randomNumberBetween(0, possibleQuestions.size() - 1);

        return possibleQuestions.get(question);

    }

    public static int randomNumberBetween(int min, int max){
        Random rand  = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

}
