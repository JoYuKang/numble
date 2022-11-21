package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.project.numble.application.image.dto.request.AddImageRequest;
import com.project.numble.application.image.dto.request.DelImageRequest;
import java.lang.reflect.Constructor;
import java.util.List;
import org.springframework.mock.web.MockMultipartFile;

public class ImageDtoFactory {

    public static AddImageRequest createCreateImageRequest(String imageFilename) {
        AddImageRequest request = null;

        try {
            Constructor<AddImageRequest> constructor = AddImageRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            request = constructor.newInstance();

            MockMultipartFile image = new MockMultipartFile("image", imageFilename, "image/jpeg", new byte[]{1});
            setField(request, "images", List.of(image));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }

    public static DelImageRequest createDeleteImageRequest(Long imageId) {
        DelImageRequest request = null;

        try {
            Constructor<DelImageRequest> constructor = DelImageRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            request = constructor.newInstance();

            setField(request, "imageId", imageId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }
}
