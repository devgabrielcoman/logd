package com.gabrielcoman.logd.models;

public class Token {

    private String fbId;
    private String firToken;

    public Token(String fbId, String firToken) {
        this.fbId = fbId;
        this.firToken = firToken;
    }

    public String getFbId () {
        return fbId;
    }

    public String getFirToken () {
        return firToken;
    }
}
