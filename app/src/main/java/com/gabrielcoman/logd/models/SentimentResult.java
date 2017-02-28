package com.gabrielcoman.logd.models;

import org.json.JSONObject;

import tv.superawesome.lib.sajsonparser.SABaseObject;
import tv.superawesome.lib.sajsonparser.SAJsonParser;

public class SentimentResult extends SABaseObject {

    private double confidence;
    private String sentiment;


    public SentimentResult(String json) {
        JSONObject jsonObject = SAJsonParser.newObject(json);
        readFromJson(jsonObject);
    }

    public SentimentResult(JSONObject jsonObject) {
        readFromJson(jsonObject);
    }

    @Override
    public void readFromJson(JSONObject json) {
        try {
            confidence = Double.parseDouble(SAJsonParser.getString(json, "confidence"));
        } catch (Exception e) {
            // do nothing
        }
        sentiment = SAJsonParser.getString(json, "sentiment");
    }

    @Override
    public JSONObject writeToJson() {
        return SAJsonParser.newObject(new Object[] {
                "confidence", confidence,
                "sentiment", sentiment
        });
    }

    public double getNormalisedSentiment () {

        try {
            switch (sentiment) {
                case "Neutral": {
                    return confidence / 100.0;
                }
                case "Positive": {
                    return confidence / 100.0;
                }
                case "Negative": {
                    return ((-1) * confidence) / 100.0;
                }
            }
        } catch (Exception e) {
            return 0.0;
        }
        return 0.0;
    }
}
