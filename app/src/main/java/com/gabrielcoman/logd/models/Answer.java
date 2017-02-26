/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.SABaseObject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class Answer extends SABaseObject {

    public enum AnswerValue {
        NEGATIVE(0),
        NEUTRAL(1),
        POSITIVE(2);

        private final int value;

        AnswerValue(int i) {
            this.value = i;
        }

        public static AnswerValue fromValue (int value) {
            return value == 2 ? POSITIVE : value == 1 ? NEUTRAL : NEGATIVE;
        }
    }

    private String name;
    private AnswerValue value;

    public Answer () {
        // do nothing
    }

    public Answer (JSONObject json) {
        readFromJson(json);
    }

    public Answer (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public String getName() {
        return name;
    }

    public AnswerValue getValue() {
        return value;
    }

    public static Answer positiveAnswer (String name) {
        Answer answer = new Answer();
        answer.name = name;
        answer.value = AnswerValue.POSITIVE;
        return answer;
    }

    public static Answer neutralAnswer (String name) {
        Answer answer = new Answer();
        answer.name = name;
        answer.value = AnswerValue.NEUTRAL;
        return answer;
    }

    public static Answer negativeAnswer (String name) {
        Answer answer = new Answer();
        answer.name = name;
        answer.value = AnswerValue.NEGATIVE;
        return answer;
    }

    @Override
    public void readFromJson(JSONObject json) {
        name = SAJsonParser.getString(json, "name");
        value = AnswerValue.fromValue(SAJsonParser.getInt(json, "value"));
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "name", name,
                "value", value.ordinal()
        });
    }
}
