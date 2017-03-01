package com.gabrielcoman.logd.system.network;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

public class Network {

    public static Observable<Response> post (String url, HashMap<String, String> body) {

        OkHttpClient client = new OkHttpClient();

        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : body.keySet()) {
            formBuilder.add(key, body.get(key));
        }
        RequestBody formBody = formBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        return Observable.create(subscriber -> {

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    subscriber.onError(new Throwable());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                }
            });

        });

    }
}
