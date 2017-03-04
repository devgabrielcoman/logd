/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

public class Response {

    private long timestamp;
    private String answer;
    private double value;

    public Response (String answer, double value) {
        this.answer = answer;
        this.value = value;
        this.timestamp = System.currentTimeMillis()/1000;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAnswer() {
        return answer;
    }

    public double getValue() {
        return value;
    }
}
