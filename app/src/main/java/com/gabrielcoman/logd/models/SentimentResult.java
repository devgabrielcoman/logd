/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

public class SentimentResult {

    private double confidence = 0.0;
    private String sentiment = null;

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
