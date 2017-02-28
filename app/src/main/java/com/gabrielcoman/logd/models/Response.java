/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

import android.os.Parcel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import tv.superawesome.lib.sajsonparser.SABaseObject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class Response extends SABaseObject {

    private long timestamp;
    private Answer answer;

    public Response () {

    }

    public Response (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public Response (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    public Response (Answer answer) {
        this.answer = answer;
        this.timestamp = System.currentTimeMillis()/1000;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public Answer getAnswer() {
        return answer;
    }

    public String getDate () {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        sdf.setTimeZone(tz);//set time zone.
        return sdf.format(new Date(timestamp * 1000));
    }

    @Override
    public void readFromJson(JSONObject json) {
        timestamp = SAJsonParser.getLong(json, "timestamp");
        JSONObject answerObj = SAJsonParser.getJsonObject(json, "answer");
        answer = new Answer(answerObj);
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "timestamp", timestamp,
                "answer", answer.writeToJson()
        });
    }
}
