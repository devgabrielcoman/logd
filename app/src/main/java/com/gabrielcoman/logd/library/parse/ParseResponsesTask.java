package com.gabrielcoman.logd.library.parse;

import com.gabrielcoman.logd.library.Task;
import com.gabrielcoman.logd.models.Response;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Single;

public class ParseResponsesTask implements Task<ParseRequest, Observable<Response>> {

    @Override
    public Observable<Response> execute(ParseRequest input) {
        return Observable.create(subscriber -> {
            try {
                //
                // create GSON object
                Gson gson = new Gson();

                //
                // parse as static array
                Response[] responses = gson.fromJson(input.contents, Response[].class);

                //
                // turn to list
                List<Response> result = Arrays.asList(responses);

                //
                // send out events
                for (Response res : result) {
                    subscriber.onNext(res);
                }
                subscriber.onCompleted();
            } catch (Exception e) {
                //
                // send out error
                subscriber.onError(e);
            }
        });
    }
}
