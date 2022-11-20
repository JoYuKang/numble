package com.project.numble.application.helper.factory.dto;

import static org.springframework.test.util.ReflectionTestUtils.*;

import com.project.numble.application.comment.dto.response.ChildCommentResponse;
import com.project.numble.application.comment.dto.response.RootCommentResponse;
import java.time.LocalDateTime;
import java.util.List;

public class RootCommentResponseFactory {

    public static RootCommentResponse createRootCommentResponse() {
        RootCommentResponse response = new RootCommentResponse();
        ChildCommentResponse child = new ChildCommentResponse();
        try {
            setField(response, "commentId", 1L);
            setField(response, "author", "author");
            setField(response, "address", "address");
            setField(response, "content", "content");
            setField(response, "depth", 1L);
            setField(response, "deleted", false);
            setField(response, "createdDate", LocalDateTime.now());

            setField(child, "commentId", 2L);
            setField(child, "author", "author");
            setField(child, "content", "content");
            setField(child, "depth", 2L);
            setField(child, "address", "address");
            setField(child, "deleted", false);
            setField(child, "createdDate", LocalDateTime.now());
            setField(child, "rootId", 1L);

            response.setChildren(List.of(child));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
