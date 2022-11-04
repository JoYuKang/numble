package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.project.numble.application.auth.dto.request.SignInRequest;
import java.lang.reflect.Constructor;

public class SignInFactory {

    public static SignInRequest createSignUpRequest(String email, String password) {
        SignInRequest request = null;

        try {
            Constructor<SignInRequest> constructor = SignInRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            request = constructor.newInstance();

            setField(request, "email", email);
            setField(request, "password", password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
