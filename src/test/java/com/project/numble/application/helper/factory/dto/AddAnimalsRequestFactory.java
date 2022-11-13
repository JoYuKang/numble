package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.*;

import com.project.numble.application.user.dto.request.AddAnimalsRequest;
import java.lang.reflect.Constructor;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;

public class AddAnimalsRequestFactory {

    public static AddAnimalsRequest newInstance(List<String> animalTypes) {
        AddAnimalsRequest request = null;

        try {
            Constructor<AddAnimalsRequest> constructor = AddAnimalsRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            request = constructor.newInstance();

            setField(request, "animalTypes", animalTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
