package com.gabrielcoman.logd.library;

import rx.Single;

public interface Task <Input extends Request, Output> {
    Output execute(Input input);
}
