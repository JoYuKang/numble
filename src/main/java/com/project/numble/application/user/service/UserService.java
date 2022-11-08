package com.project.numble.application.user.service;

import com.project.numble.application.auth.dto.request.SignUpRequest;
import com.project.numble.application.user.dto.response.FindAddressByClientIpResponse;
import com.project.numble.application.user.dto.response.FindAddressByQueryResponse;

public interface UserService {

    void signUp(SignUpRequest request);

    FindAddressByClientIpResponse searchAddressByIp();

    FindAddressByQueryResponse searchAddressByQuery(String query);
}
