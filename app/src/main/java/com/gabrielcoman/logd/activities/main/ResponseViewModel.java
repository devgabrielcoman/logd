/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.main;

import android.util.Log;

import com.gabrielcoman.logd.models.Response;

import java.util.Calendar;

class ResponseViewModel implements Comparable {

    private long hourTimestamp;
    private long dayTimestamp;
    private String answer;
    private double value;

    ResponseViewModel(Response response) {
        answer = response.getAnswer();
        value = response.getValue();
        hourTimestamp = response.getTimestamp();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(hourTimestamp * 1000);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        Calendar cal2 = Calendar.getInstance();
        cal2.set(year, month, day, 0, 0, 0);
        dayTimestamp = (cal2.getTimeInMillis() / 1000);
    }

    String getAnswer() {
        return answer;
    }

    public double getValue() {
        return value;
    }

    long getDayTimestamp() {
        return dayTimestamp;
    }

    String getHour() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(hourTimestamp * 1000);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String sHour = hour == 0 ? "00" : "" + hour;
        String sMinute = minute == 0 ? "00": "" + minute;
        return "@" + sHour + ":" + sMinute;
    }

    @Override
    public int compareTo(Object o) {
        ResponseViewModel r = (ResponseViewModel) o;
        return hourTimestamp > r.hourTimestamp ? -1 : 1;
    }

    public boolean isSameDay (ResponseViewModel viewModel) {
        return dayTimestamp == viewModel.dayTimestamp;
    }
}
