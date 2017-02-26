/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

import android.content.Context;
import android.graphics.Color;

import com.gabrielcoman.logd.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tv.superawesome.lib.sajsonparser.SABaseObject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.sajsonparser.SAJsonToList;

public class Question extends SABaseObject {

    private String title;
    private List<Answer> possibleAnswers = new ArrayList<>();

    public Question () {
        // do nothing
    }

    public Question (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    public Question (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public String getTitle() {
        return title;
    }

    public List<Answer> getPossibleAnswers() {
        return possibleAnswers;
    }

    public static Question morningQuestion1 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_morning_1);
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_morning_1_answer_1)));
        question.possibleAnswers.add(Answer.negativeAnswer(context.getString(R.string.data_question_morning_1_answer_2)));
        return question;
    }

    public static Question morningQuestion2 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_morning_2);
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_morning_2_answer_1)));
        question.possibleAnswers.add(Answer.neutralAnswer(context.getString(R.string.data_question_morning_2_answer_2)));
        question.possibleAnswers.add(Answer.negativeAnswer(context.getString(R.string.data_question_morning_2_answer_3)));
        return question;
    }

    public static Question morningQuestion3 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_morning_3);
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_morning_3_answer_1)));
        question.possibleAnswers.add(Answer.neutralAnswer(context.getString(R.string.data_question_morning_3_answer_2)));
        question.possibleAnswers.add(Answer.negativeAnswer(context.getString(R.string.data_question_morning_3_answer_3)));
        return question;
    }

    public static Question eveningQuestion1 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_evening_1);
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_evening_1_answer_1)));
        question.possibleAnswers.add(Answer.neutralAnswer(context.getString(R.string.data_question_evening_1_answer_2)));
        question.possibleAnswers.add(Answer.negativeAnswer(context.getString(R.string.data_question_evening_1_answer_3)));
        return question;
    }

    public static Question eveningQuestion2 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_evening_2);
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_evening_2_answer_1)));
        question.possibleAnswers.add(Answer.negativeAnswer(context.getString(R.string.data_question_evening_2_answer_2)));
        return question;
    }

    public static Question eveningQuestion3 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_evening_3);
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_evening_3_answer_1)));
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_evening_3_answer_2)));
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_evening_3_answer_3)));
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_evening_3_answer_4)));
        return question;
    }

    public static Question eveningQuestion4 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_evening_4);
        question.possibleAnswers.add(Answer.positiveAnswer(context.getString(R.string.data_question_evening_4_answer_1)));
        question.possibleAnswers.add(Answer.negativeAnswer(context.getString(R.string.data_question_evening_4_answer_2)));
        return question;
    }

    @Override
    public void readFromJson(JSONObject json) {
        title = SAJsonParser.getString(json, "title");
        possibleAnswers = SAJsonParser.getListFromJsonArray(json, "possibleAnswers", new SAJsonToList<Answer, JSONObject>() {
            @Override
            public Answer traverseItem(JSONObject jsonObject) {
                return new Answer(jsonObject);
            }
        });

    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "title", title,
                "possibleAnswers", SAJsonParser.getJsonArrayFromList(possibleAnswers, Answer::writeToJson)
        });
    }
}
