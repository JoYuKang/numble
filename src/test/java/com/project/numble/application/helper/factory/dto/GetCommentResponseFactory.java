package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.*;

import com.project.numble.application.comment.dto.response.GetCommentResponse;
import java.lang.reflect.Constructor;
import java.time.LocalDateTime;

public class GetCommentResponseFactory {

    public static GetCommentResponse createGetCommentResponse() {
        GetCommentResponse response = null;

        try {
            Constructor<GetCommentResponse> constructor = GetCommentResponse.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            response = constructor.newInstance();

            setField(response, "commentId", 1L);
            setField(response, "author", "author");
            setField(response, "content", "content");
            setField(response, "depth", 1L);
            setField(response, "createdDate", LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
