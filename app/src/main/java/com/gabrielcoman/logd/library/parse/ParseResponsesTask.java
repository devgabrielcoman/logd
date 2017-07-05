package com.gabrielcoman.logd.library.parse;

import com.gabrielcoman.logd.library.Task;
import com.gabrielcoman.logd.models.Response;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import rx.Single;


public class ParseResponsesTask implements Task<ParseRequest, List<Response>> {

    @Override
    public Single<List<Response>> execute(ParseRequest input) {
        return Single.create(subscriber -> {
            try {
                Gson gson = new Gson();
                Response[] responses = gson.fromJson(input.contents, Response[].class);
                subscriber.onSuccess(Arrays.asList(responses));
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
