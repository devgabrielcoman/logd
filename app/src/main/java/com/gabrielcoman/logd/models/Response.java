/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

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
    private String answer;
    private double value;

    public Response () {
        // do nothing
    }

    public Response (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public Response (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    public Response (String answer, double value) {
        this.answer = answer;
        this.value = value;
        this.timestamp = System.currentTimeMillis()/1000;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAnswer() {
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
        answer = SAJsonParser.getString(json, "answer");
        value = SAJsonParser.getDouble(json, "value");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "timestamp", timestamp,
                "answer", answer,
                "value", value
        });
    }

    public double getValue() {
        return value;
    }
}
