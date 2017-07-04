package com.gabrielcoman.logd.library.network;

import android.support.annotation.NonNull;

import com.gabrielcoman.logd.models.Token;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SendTokenRequest implements NetworkRequest {

    private String fbId;
    private String firToken;

    @NonNull
    @Override
    public String getUrl() {
        return "https://us-central1-logd-da13f.cloudfunctions.net";
    }

    @NonNull
    @Override
    public String getEndpoint() {
        return "/saveToken";
    }

    @NonNull
    @Override
    public Map<String, Object> getQuery() {
        return ImmutableMap.of("fbId", fbId, "token", firToken);
    }

    @NonNull
    @Override
    public Map<String, String> getBody() {
        return ImmutableMap.of();
    }

    public SendTokenRequest(Token token) {
        this.fbId = token.getFbId();
        this.firToken = token.getFirToken();
    }
}
