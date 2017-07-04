package com.gabrielcoman.logd.library.network;

import android.support.annotation.NonNull;

import com.gabrielcoman.logd.library.Request;

import java.util.Map;

public interface NetworkRequest extends Request {
    @NonNull String getUrl();
    @NonNull String getEndpoint();
    @NonNull Map<String, Object> getQuery();
    @NonNull Map<String, String> getBody();
}
