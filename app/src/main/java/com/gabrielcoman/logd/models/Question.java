/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

import android.content.Context;

import com.gabrielcoman.logd.R;

import org.json.JSONObject;
import org.xml.sax.SAXNotRecognizedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import tv.superawesome.lib.sajsonparser.SABaseObject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;
import tv.superawesome.lib.sajsonparser.SAJsonToList;
import tv.superawesome.lib.sajsonparser.SAListToJson;

public class Question extends SABaseObject {

    private String title;
    private List<String> possibleAnswers = new ArrayList<>();

    public Question () {
        // do nothing
    }

    public Question (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public Question (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    public String getTitle() {
        return title;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public static Question morningQuestion1 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_morning_1);
        question.possibleAnswers = Arrays.asList(
                context.getString(R.string.data_question_morning_1_answer_1),
                context.getString(R.string.data_question_morning_1_answer_2),
                context.getString(R.string.data_question_general_answer_journal));
        return question;
    }

    public static Question morningQuestion2 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_morning_2);
        question.possibleAnswers = Arrays.asList(
                context.getString(R.string.data_question_morning_2_answer_1),
                context.getString(R.string.data_question_morning_2_answer_2),
                context.getString(R.string.data_question_morning_2_answer_3),
                context.getString(R.string.data_question_general_answer_journal));
        return question;
    }

    public static Question morningQuestion3 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_morning_3);
        question.possibleAnswers = Arrays.asList(
                context.getString(R.string.data_question_morning_3_answer_1),
                context.getString(R.string.data_question_morning_3_answer_2),
                context.getString(R.string.data_question_morning_3_answer_3),
                context.getString(R.string.data_question_general_answer_journal));
        return question;
    }

    public static Question eveningQuestion1 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_evening_1);
        question.possibleAnswers = Arrays.asList(
                context.getString(R.string.data_question_evening_1_answer_1),
                context.getString(R.string.data_question_evening_1_answer_2),
                context.getString(R.string.data_question_evening_1_answer_3),
                context.getString(R.string.data_question_general_answer_journal));
        return question;
    }

    public static Question eveningQuestion2 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_evening_2);
        question.possibleAnswers = Arrays.asList(
                context.getString(R.string.data_question_evening_2_answer_1),
                context.getString(R.string.data_question_evening_2_answer_2),
                context.getString(R.string.data_question_general_answer_journal));
        return question;
    }

    public static Question eveningQuestion3 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_evening_3);

        question.possibleAnswers = Arrays.asList(
                context.getString(R.string.data_question_evening_3_answer_1),
                context.getString(R.string.data_question_evening_3_answer_2),
                context.getString(R.string.data_question_evening_3_answer_3),
                context.getString(R.string.data_question_evening_3_answer_4),
                context.getString(R.string.data_question_general_answer_journal));
        return question;
    }

    public static Question eveningQuestion4 (Context context) {
        Question question = new Question();
        question.title = context.getString(R.string.data_question_evening_4);
        question.possibleAnswers = Arrays.asList(
                context.getString(R.string.data_question_evening_4_answer_1),
                context.getString(R.string.data_question_evening_4_answer_2),
                context.getString(R.string.data_question_general_answer_journal));
        return question;
    }

    @Override
    public void readFromJson(JSONObject json) {
        title = SAJsonParser.getString(json, "title");
        possibleAnswers = SAJsonParser.getListFromJsonArray(json, "possibleAnswers", new SAJsonToList<String, String>() {
            @Override
            public String traverseItem(String jsonObject) {
                return jsonObject;
            }
        });
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "title", title,
                "possibleAnswers", SAJsonParser.getJsonArrayFromList(possibleAnswers, s -> s)
        });
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
