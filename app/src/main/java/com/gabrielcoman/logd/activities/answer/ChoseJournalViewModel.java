/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (dev.gabriel.coman@gmail.com)
 */
package com.gabrielcoman.logd.activities.answer;

import android.content.Context;

import com.gabrielcoman.logd.R;

public class ChoseJournalViewModel {

    private String title;

    public ChoseJournalViewModel (Context context) {
        title = context.getString(R.string.data_question_general_answer_journal);
    }

    public String getTitle() {
        return title;
    }
}
