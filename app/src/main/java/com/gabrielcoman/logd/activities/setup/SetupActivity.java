package com.gabrielcoman.logd.activities.setup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabrielcoman.logd.R;
import com.gabrielcoman.logd.activities.BaseActivity;
import com.gabrielcoman.logd.activities.main.MainActivity;
import com.gabrielcoman.logd.library.notification.SubscribeToTopicRequest;
import com.gabrielcoman.logd.library.notification.SubscribeToTopicTask;
import com.gabrielcoman.logd.library.profile.GetProfileRequest;
import com.gabrielcoman.logd.library.profile.GetProfileTask;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import rx.Single;

public class SetupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        ImageView profileImage = (ImageView) findViewById(R.id.ProfilePicture);
        TextView profileName = (TextView) findViewById(R.id.ProfileName);
        Button gotIt = (Button) findViewById(R.id.GotItButton);

        SubscribeToTopicRequest request1 = new SubscribeToTopicRequest("morning_questions");
        SubscribeToTopicRequest request2 = new SubscribeToTopicRequest("evening_questions");
        SubscribeToTopicTask task1 = new SubscribeToTopicTask();

        GetProfileRequest request3 = new GetProfileRequest();
        GetProfileTask task2 = new GetProfileTask();

        Single.zip(task1.execute(request1), task1.execute(request2), task2.execute(request3), (aVoid, aVoid2, profile) -> profile)
                .subscribe(profile -> {

                    //
                    // setup picture
                    Picasso.with(SetupActivity.this)
                            .load(profile.getProfilePictureUri(260, 260))
                            .placeholder(R.drawable.ic_user_default)
                            .error(R.drawable.ic_user_default)
                            .transform(new CropCircleTransformation())
                            .into(profileImage);

                    //
                    // setup profile
                    profileName.setText(getString(R.string.activity_setup_welcome_user, profile.getName()));

                }, throwable -> {
                    Log.e("Logd", "Error is " + throwable.getMessage());
                });

        //
        // continue
        RxView.clicks(gotIt)
                .subscribe(aVoid -> {
                    Intent intent = new Intent(SetupActivity.this, MainActivity.class);
                    SetupActivity.this.startActivity(intent);
                });

    }
}
