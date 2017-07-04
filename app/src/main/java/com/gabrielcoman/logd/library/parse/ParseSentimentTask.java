package com.gabrielcoman.logd.library.parse;

import com.gabrielcoman.logd.library.Task;
import com.gabrielcoman.logd.models.Sentiment;
import com.google.gson.Gson;

import rx.Single;

public class ParseSentimentTask implements Task<ParseRequest, Double> {

    @Override
    public Single<Double> execute(ParseRequest input) {
        return Single.create(subscriber-> {

            try {
                Gson gson = new Gson();
                Sentiment sentiment = gson.fromJson(input.contents, Sentiment.class);
                subscriber.onSuccess(sentiment.getResult().getNormalisedSentiment());
            } catch (Exception e) {
                subscriber.onError(new Throwable());
            }
        });
    }
}
