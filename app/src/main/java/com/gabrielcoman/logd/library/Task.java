package com.gabrielcoman.logd.library;

import rx.Single;

public interface Task <Input extends Request, Output> {
    Single<Output> execute(Input input);
}
