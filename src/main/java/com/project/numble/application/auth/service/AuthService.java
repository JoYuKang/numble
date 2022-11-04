package com.project.numble.application.auth.service;

import com.project.numble.application.auth.dto.request.SignInRequest;

public interface AuthService {

    void signIn(SignInRequest request);
}
