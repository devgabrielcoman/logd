package com.gabrielcoman.logd.network;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Single;

public class GetAnswers {

    private static final String base = "https://us-central1-logd-da13f.cloudfunctions.net/getAnswers";

    public Single<Response> execute(boolean isMorning, String question) {

        //
        // form url
        String url = GetAnswers.base + "?isMorning=" + isMorning + "&question=" + question;

        //
        // create the client
        OkHttpClient client = new OkHttpClient();

        //
        // build the request
        Request request = new Request.Builder().url(url).build();

        //
        // execute & return
        return Single.create(subscriber -> {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    subscriber.onError(new Throwable());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    subscriber.onSuccess(response);
                }
            });
        });
    }
}
