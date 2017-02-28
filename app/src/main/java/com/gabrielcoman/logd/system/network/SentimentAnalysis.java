package com.gabrielcoman.logd.system.network;

import android.content.Context;
import android.util.Log;

import com.gabrielcoman.logd.models.Sentiment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

public class SentimentAnalysis {

    public static Observable<Double> analyseSentiment (String text) {

        // should be a singleton
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder formBuilder = new FormBody.Builder().add("txt", text);
        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url("http://sentiment.vivekn.com/api/text/")
                .post(formBody)
                .build();

        return Observable.create(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(Subscriber<? super Double> subscriber) {

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        subscriber.onError(new Throwable());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        if (response != null) {
                            String result = response.body().string();
                            Log.d("Logd-App", result);
                            Sentiment sentiment = new Sentiment(result);
                            double value = sentiment.getResult().getNormalisedSentiment();
                            subscriber.onNext(value);
                            subscriber.onCompleted();
                        }
                    }
                });

            }
        });

    }

}
