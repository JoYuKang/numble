package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.*;

import com.project.numble.application.comment.dto.request.ReplyCommentRequest;

public class ReplyCommentRequestFactory {

    public static ReplyCommentRequest createReplyCommentRequest(Long boardId, Long commentId, String content) {
        ReplyCommentRequest request = new ReplyCommentRequest();

        try {
            setField(request, "boardId", boardId);
            setField(request, "commentId", commentId);
            setField(request, "content", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }
}
