package com.project.numble.application.helper.factory.dto;

import com.project.numble.application.user.dto.response.GetMyInfoResponse;
import java.lang.reflect.Constructor;
import java.util.List;

public class GetMyInfoResponseFactory {

    public static final GetMyInfoResponse infoBy(String nickname, String profile, String regionDepth2, List<String> animals) {
        GetMyInfoResponse response = null;

        try {
            Constructor<GetMyInfoResponse> constructor = GetMyInfoResponse.class.getDeclaredConstructor(
                String.class, String.class, String.class, List.class);

            constructor.setAccessible(true);
            response = constructor.newInstance(nickname, profile, regionDepth2, animals);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
