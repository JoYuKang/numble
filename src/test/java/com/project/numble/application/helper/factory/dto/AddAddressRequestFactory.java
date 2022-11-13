package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.project.numble.application.user.dto.request.AddAddressRequest;
import java.lang.reflect.Constructor;

public class AddAddressRequestFactory {

    public static AddAddressRequest newInstance(String addressName, String regionDepth1, String regionDepth2) {
        AddAddressRequest request = null;

        try {
            Constructor<AddAddressRequest> constructor = AddAddressRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            request = constructor.newInstance();

            setField(request, "addressName", addressName);
            setField(request, "regionDepth1", regionDepth1);
            setField(request, "regionDepth2", regionDepth2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
