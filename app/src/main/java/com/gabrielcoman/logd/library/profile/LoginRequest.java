package com.gabrielcoman.logd.library.profile;

import com.gabrielcoman.logd.library.Request;

import java.util.Arrays;
import java.util.Collection;

public class LoginRequest implements Request {

    final Collection<String> permissions = Arrays.asList("public_profile");

}
