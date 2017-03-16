package com.gabrielcoman.logd.system.api;

import com.gabrielcoman.logd.models.Sentiment;
import com.gabrielcoman.logdnetwork.Network;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import rx.Observable;
import rx.Single;

public class SentimentAPI {

    public static Single<Double> analyseSentiment (String text) {

        String url = "http://sentiment.vivekn.com/api/text/";
        HashMap<String, String> body = new HashMap<>();
        body.put("txt", text);

        return Network.post(url, body)
                .map(response -> {
                    try {
                        return response.body().string();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .map(s -> new Gson().fromJson(s, Sentiment.class))
                .map(sentiment -> sentiment.getResult().getNormalisedSentiment());
    }
}
