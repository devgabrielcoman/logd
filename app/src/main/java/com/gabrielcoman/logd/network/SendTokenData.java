package com.gabrielcoman.logd.network;

import com.gabrielcoman.logd.models.Token;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Single;

public class SendTokenData {

    private static final String base = "https://us-central1-logd-da13f.cloudfunctions.net/saveToken";

    public Single<Response> execute (Token token) {
        String url = SendTokenData.base + "?fbId=" + token.getFbId() + "&token=" + token.getFirToken();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

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
