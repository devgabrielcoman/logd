package com.gabrielcoman.logd.library.network;

import android.text.TextUtils;
import android.util.Log;

import com.gabrielcoman.logd.library.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Single;

public class NetworkTask <Input extends NetworkRequest> implements Task <Input, String> {

    @Override
    public Single<String> execute(Input input) {

        //
        // create client
        OkHttpClient client = new OkHttpClient();

        //
        // get Url
        String url = input.getUrl();

        //
        // get endpoint
        String endpoint = input.getEndpoint();

        //
        // form the query
        List<String> items = new ArrayList<>();
        for (String key : input.getQuery().keySet()) {
            items.add(key + "=" + input.getQuery().get(key));
        }
        String query = TextUtils.join("&", items);

        //
        // form the final call url
        String call = url + endpoint + "?" + query;

        //
        // form the build body
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : input.getBody().keySet()) {
            formBuilder.add(key, input.getBody().get(key));
        }
        RequestBody formBody = formBuilder.build();

        //
        // form the final request
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(call)
                .post(formBody)
                .build();

        return Single.create(subscriber -> {

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    subscriber.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        subscriber.onSuccess(response.body().string());
                    } catch (Exception e) {
                        subscriber.onError(e);
                    } finally {
                        response.close();
                    }
                }
            });
        });
    }
}
