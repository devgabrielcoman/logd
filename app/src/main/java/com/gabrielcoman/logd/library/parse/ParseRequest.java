package com.gabrielcoman.logd.library.parse;

import com.gabrielcoman.logd.library.Request;

public class ParseRequest implements Request {

    public String contents;

    public ParseRequest(String contents) {
        this.contents = contents;
    }
}
