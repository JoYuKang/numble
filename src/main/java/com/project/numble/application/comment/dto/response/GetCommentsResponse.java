package com.project.numble.application.comment.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetCommentsResponse {
    private List<GetCommentResponse> comments;
}
