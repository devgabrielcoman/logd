package com.gabrielcoman.logd.library.network;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class GetSentimentRequest implements NetworkRequest {

    private String text;

    @NonNull
    @Override
    public String getUrl() {
        return "http://sentiment.vivekn.com";
    }

    @NonNull
    @Override
    public String getEndpoint() {
        return "/api/text/";
    }

    @NonNull
    @Override
    public Map<String, Object> getQuery() {
        return ImmutableMap.of();
    }

    @NonNull
    @Override
    public Map<String, String> getBody() {
        return ImmutableMap.of("txt", text);
    }

    public GetSentimentRequest(String text) {
        this.text = text;
    }
}
