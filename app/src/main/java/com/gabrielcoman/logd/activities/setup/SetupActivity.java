package com.gabrielcoman.logd.activities.setup;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.facebook.Profile;
import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;

public class SetupActivity extends BaseActivity {

    private static final int REQUEST = 1;
    private static final String PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Profile profile = Profile.getCurrentProfile();
    }

    //
    // enable permissions
    public void enableNotifications (View view) {
        ActivityCompat.requestPermissions(this, new String[]{PERMISSION}, REQUEST);
    }
}
