/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.main;

import com.gabrielcoman.logd.models.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ResponseViewModel implements Comparable {

    private long timestamp;
    private String answer;
    private String value;
    private String date;

    public ResponseViewModel (Response response) {
        answer = response.getAnswer();
        value = "" + response.getValue();
        timestamp = response.getTimestamp();

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        sdf.setTimeZone(tz);//set time zone.
        date = sdf.format(new Date(timestamp * 1000));
    }

    public String getAnswer() {
        return answer;
    }

    public String getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int compareTo(Object o) {
        ResponseViewModel r = (ResponseViewModel) o;
        return timestamp > r.timestamp ? -1 : 1;
    }
}
