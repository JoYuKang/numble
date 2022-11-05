package com.project.numble.application.user.service;

import com.project.numble.application.auth.dto.request.SignUpRequest;

public interface UserService {

    void signUp(SignUpRequest request);
}
