package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.*;

import com.project.numble.application.comment.dto.request.ModCommentRequest;

public class ModCommentRequestFactory {

    public static ModCommentRequest createModCommentRequest(Long commentId, String content) {
        ModCommentRequest request = new ModCommentRequest();

        try {
            setField(request, "commentId", commentId);
            setField(request, "content", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
