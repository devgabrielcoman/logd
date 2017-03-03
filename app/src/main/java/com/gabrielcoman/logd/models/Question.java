/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String title;
    private List<String> answers = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public List<String> getAnswers() {
        return answers;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
