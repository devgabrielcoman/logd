package com.gabrielcoman.logd.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action0;

public class BaseActivity extends AppCompatActivity {

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

    public Single<String> getStringExtras (String key) {
        return Single.create(singleSubscriber -> {

            Bundle bundle = BaseActivity.this.getIntent().getExtras();
            if (bundle != null) {
                String extra = bundle.getString(key);
                if (extra != null) {
                    singleSubscriber.onSuccess(extra);
                }
            }

        });
    }
}
