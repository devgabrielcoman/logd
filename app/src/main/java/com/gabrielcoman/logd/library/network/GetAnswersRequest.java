package com.gabrielcoman.logd.library.network;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class GetAnswersRequest implements NetworkRequest {

    private String question;
    private Boolean isMorning;

    @NonNull
    @Override
    public String getUrl() {
        return "https://us-central1-logd-da13f.cloudfunctions.net";
    }

    @NonNull
    @Override
    public String getEndpoint() {
        return "/getAnswers";
    }

    @NonNull
    @Override
    public Map<String, Object> getQuery() {
        return ImmutableMap.of("question", question, "isMorning", isMorning);
    }

    @NonNull
    @Override
    public Map<String, String> getBody() {
        return ImmutableMap.of();
    }

    public GetAnswersRequest(String question, boolean isMorning) {
        this.question = question;
        this.isMorning = isMorning;
    }
}
