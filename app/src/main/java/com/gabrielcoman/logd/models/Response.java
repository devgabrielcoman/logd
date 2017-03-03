/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Response implements Comparable {

    private long timestamp;
    private String answer;
    private double value;

    public Response (String answer, double value) {
        this.answer = answer;
        this.value = value;
        this.timestamp = System.currentTimeMillis()/1000;
    }

    public String getDate () {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        sdf.setTimeZone(tz);//set time zone.
        return sdf.format(new Date(timestamp * 1000));
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAnswer() {
        return answer;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(Object o) {
        Response r = (Response) o;
        if (timestamp > r.getTimestamp()) return -1;
        if (timestamp < r.getTimestamp()) return 1;
        return 0;
    }
}
