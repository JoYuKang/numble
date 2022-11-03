package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.*;

import com.project.numble.application.auth.dto.request.SignUpRequest;
import java.lang.reflect.Constructor;

public class SignUpFactory {

    public static SignUpRequest createSignUpRequest(String email, String password, String nickname) {
        SignUpRequest request = null;

        try {
            Constructor<SignUpRequest> constructor = SignUpRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            request = constructor.newInstance();

            setField(request, "email", email);
            setField(request, "password", password);
            setField(request, "nickname", nickname);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
