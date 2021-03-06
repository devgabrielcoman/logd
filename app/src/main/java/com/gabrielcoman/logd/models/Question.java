/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.models;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String question;
    private List<String> answers = new ArrayList<>();

    public Question (String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    @Override
    public int hashCode() {
        return question.hashCode();
    }
}
