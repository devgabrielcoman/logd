package com.gabrielcoman.logd.models;


import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.SABaseObject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class Sentiment extends SABaseObject {

    private SentimentResult result;

    public Sentiment (String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public Sentiment (JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    @Override
    public void readFromJson(JSONObject json) {
        JSONObject resultJson = SAJsonParser.getJsonObject(json, "result");
        result = new SentimentResult(resultJson);
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "result", result.writeToJson()
        });
    }

    public SentimentResult getResult() {
        return result;
    }
}
