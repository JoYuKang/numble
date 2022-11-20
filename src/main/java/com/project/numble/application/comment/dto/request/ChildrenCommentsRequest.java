package com.project.numble.application.comment.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class ChildrenCommentsRequest {

    @NotNull(message="{id.notNull}")
    @Positive(message = "{id.positive}")
    private Long boardId;

    @NotNull(message="{id.notNull}")
    @Positive(message = "{id.positive}")
    private Long rootCommentId;

    private Long lastChildCommentId;
}
