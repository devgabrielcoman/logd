package com.gabrielcoman.logd.activities;

import android.app.Activity;
import android.content.Intent;

import rx.functions.Action0;

public class BaseActivity extends Activity {

    private Action0 onActivityResult = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (onActivityResult != null && resultCode == RESULT_OK) {
            onActivityResult.call();
        }
    }

    public void setOnActivityResult(Action0 onActivityResult) {
        this.onActivityResult = onActivityResult;
    }

    public void finishOK () {
        setResult(RESULT_OK);
        finish();
    }
}
