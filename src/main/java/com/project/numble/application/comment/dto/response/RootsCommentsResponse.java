package com.project.numble.application.comment.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RootsCommentsResponse {

    private List<RootCommentResponse> comments;
}
