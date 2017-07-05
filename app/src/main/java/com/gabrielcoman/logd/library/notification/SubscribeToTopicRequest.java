package com.gabrielcoman.logd.library.notification;

import com.gabrielcoman.logd.library.Request;

public class SubscribeToTopicRequest implements Request {

    String topic;

    public SubscribeToTopicRequest(String topic) {
        this.topic = topic;
    }

}
