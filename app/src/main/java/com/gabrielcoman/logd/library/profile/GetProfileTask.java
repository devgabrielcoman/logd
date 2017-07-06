package com.gabrielcoman.logd.library.profile;

import com.facebook.Profile;
import com.gabrielcoman.logd.library.Task;

import rx.Single;

public class GetProfileTask implements Task <GetProfileRequest, Single<Profile>> {

    @Override
    public Single<Profile> execute(GetProfileRequest getProfileRequest) {

        Profile profile = Profile.getCurrentProfile();

        return Single.create(subscriber -> {

            if (profile != null) {
                subscriber.onSuccess(profile);
            } else {
                subscriber.onError(new Throwable());
            }
        });
    }
}
