package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.*;

import com.project.numble.application.comment.dto.request.AddCommentRequest;
import java.lang.reflect.Constructor;

public class AddCommentRequestFactory {

    public static AddCommentRequest createAddCommentRequest(Long boardId, String content) {
        AddCommentRequest request = null;

        try {
            Constructor<AddCommentRequest> constructor = AddCommentRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            request = constructor.newInstance();

            setField(request, "boardId", boardId);
            setField(request, "content", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
