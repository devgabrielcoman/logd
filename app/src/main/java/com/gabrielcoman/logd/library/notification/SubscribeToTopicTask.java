package com.gabrielcoman.logd.library.notification;

import com.gabrielcoman.logd.library.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import rx.Single;

public class SubscribeToTopicTask implements Task <SubscribeToTopicRequest, Void> {

    @Override
    public Single<Void> execute(SubscribeToTopicRequest input) {
        return Single.create(subscriber -> {

            FirebaseMessaging.getInstance().subscribeToTopic(input.topic);
            subscriber.onSuccess(null);
        });
    }
}
