package com.gabrielcoman.logd.library.network;

import android.support.annotation.NonNull;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class GetResponsesRequest implements NetworkRequest {

    private String fbId;

    @NonNull
    @Override
    public String getUrl() {
        return "https://us-central1-logd-da13f.cloudfunctions.net";
    }

    @NonNull
    @Override
    public String getEndpoint() {
        return "/getResponses";
    }

    @NonNull
    @Override
    public Map<String, Object> getQuery() {
        return ImmutableMap.of("fbId", fbId);
    }

    @NonNull
    @Override
    public Map<String, String> getBody() {
        return ImmutableMap.of();
    }

    public GetResponsesRequest(String fbId) {
        this.fbId = fbId;
    }
}
