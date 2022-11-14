package com.project.numble.application.user.service;

import com.project.numble.application.auth.dto.request.SignUpRequest;
import com.project.numble.application.user.dto.request.AddAddressRequest;
import com.project.numble.application.user.dto.request.AddAnimalsRequest;
import com.project.numble.application.user.dto.response.FindAddressByClientIpResponse;
import com.project.numble.application.user.dto.response.FindAddressByQueryResponse;
import com.project.numble.application.user.dto.response.GetAddressResponse;
import com.project.numble.application.user.dto.response.GetUserStaticInfoResponse;
import com.project.numble.core.resolver.UserInfo;

public interface UserService {

    void signUp(SignUpRequest request);

    FindAddressByClientIpResponse searchAddressByIp();

    FindAddressByQueryResponse searchAddressByQuery(String query);

    void addAddress(UserInfo userInfo, AddAddressRequest request);

    GetAddressResponse getAddress(UserInfo userInfo);

    void addAnimals(UserInfo userInfo, AddAnimalsRequest request);

    GetUserStaticInfoResponse getUserStaticInfo(UserInfo userInfo);

    void delAddress(UserInfo userInfo);

    void withdrawalUser(UserInfo userInfo);
}
