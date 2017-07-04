package com.gabrielcoman.logd.library.network;

import android.support.annotation.NonNull;

import com.gabrielcoman.logd.models.Response;
import com.google.common.collect.ImmutableMap;

import java.util.Map;


public class AddResponseRequest implements NetworkRequest {

    private String fbId;
    private String answer;
    private long timestamp;
    private double value;

    @NonNull
    @Override
    public String getUrl() {
        return "https://us-central1-logd-da13f.cloudfunctions.net";
    }

    @NonNull
    @Override
    public String getEndpoint() {
        return "/addResponse";
    }

    @NonNull
    @Override
    public Map<String, Object> getQuery() {
        return ImmutableMap.of("fbId", fbId, "answer", answer, "timestamp", timestamp, "value", value);
    }

    @NonNull
    @Override
    public Map<String, String> getBody() {
        return ImmutableMap.of();
    }

    public AddResponseRequest(String fbId, Response response) {
        this.fbId = fbId;
        this.timestamp = response.getTimestamp();
        this.answer = response.getAnswer();
        this.value = response.getValue();
    }
}
