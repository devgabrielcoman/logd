/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.SABaseObject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class Answer extends SABaseObject {

    private double value = 0.0;
    protected String title = null;
    protected String text = null;

    public Answer () {
    }

    public Answer (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public Answer (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    public String getTitle() {
        return title;
    }

    public double getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public void updateText (String text) {
        this.title = text;
    }

    public static Answer journalAnswer (String title) {
        Answer answer = new Answer();
        answer.title = title;
        return answer;
    }

    public static Answer positiveAnswer (String title) {
        Answer answer = new Answer();
        answer.title = title;
        answer.value = 1.0;
        return answer;
    }

    public static Answer neutralAnswer (String title) {
        Answer answer = new Answer();
        answer.title = title;
        answer.value = 0.5;
        return answer;
    }

    public static Answer negativeAnswer (String title) {
        Answer answer = new Answer();
        answer.title = title;
        answer.value = 0.0;
        return answer;
    }

    @Override
    public void readFromJson(JSONObject json) {
        title = SAJsonParser.getString(json, "title");
        text = SAJsonParser.getString(json, "text");
        value = SAJsonParser.getDouble(json, "value");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "title", title,
                "text", text,
                "value", value
        });
    }
}
