package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.project.numble.application.user.dto.request.AddAddressRequest;
import com.project.numble.application.user.dto.response.GetAddressResponse;
import java.lang.reflect.Constructor;

public class GetAddressResponseFactory {

    public static GetAddressResponse newInstance(String addressName, String regionDepth1, String regionDepth2) {
        GetAddressResponse request = null;

        try {
            Constructor<GetAddressResponse> constructor = GetAddressResponse.class.getDeclaredConstructor(String.class, String.class, String.class);
            constructor.setAccessible(true);

            request = constructor.newInstance(addressName, regionDepth1, regionDepth2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
