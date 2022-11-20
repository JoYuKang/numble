package com.project.numble.application.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChildrenCommentsResponse {

    List<ChildCommentResponse> comments;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long lastChildCommentId;

    public static ChildrenCommentsResponse fromComments(List<ChildCommentResponse> comments) {
        if (comments.size() == 0) {
            return new ChildrenCommentsResponse(comments, null);
        }
        else {
            return new ChildrenCommentsResponse(comments, comments.get(comments.size() - 1).getCommentId());
        }
    }
}
