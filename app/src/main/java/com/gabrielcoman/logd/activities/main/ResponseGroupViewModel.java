package com.gabrielcoman.logd.activities.main;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

class ResponseGroupViewModel implements Comparable {

    private Calendar calendar;

    private long dayTimestamp;
    private List<ResponseViewModel> viewModels;

    ResponseGroupViewModel(long dayTimestamp, List<ResponseViewModel> viewModels) {
        this.dayTimestamp = dayTimestamp;
        this.viewModels = viewModels;
        Collections.sort(this.viewModels);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dayTimestamp * 1000);
    }

    List<ResponseViewModel> getViewModels() {
        return viewModels;
    }

    String getDayOfMonth() {
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth <= 9 ? ("0" + dayOfMonth) : ("" + dayOfMonth);
    }

    String getMonthYear() {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);

        return month + " " + year;
    }

    @Override
    public int compareTo(Object o) {
        ResponseGroupViewModel r = (ResponseGroupViewModel) o;
        return dayTimestamp > r.dayTimestamp ? -1 : 1;
    }
}
