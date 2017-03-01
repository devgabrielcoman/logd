package com.gabrielcoman.logd.system.api;

import com.gabrielcoman.logd.models.Sentiment;
import com.gabrielcoman.logd.system.network.Network;

import java.io.IOException;
import java.util.HashMap;

import rx.Observable;

public class SentimentAnalysis {

    public static Observable<Double> analyseSentiment (String text) {

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
                .map(Sentiment::new)
                .map(sentiment -> sentiment.getResult().getNormalisedSentiment());
    }
}
