package com.project.numble.application.comment.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetRootCommentsRequest {

    @NotNull(message="{id.notNull}")
    @Positive(message = "{id.positive}")
    private Long boardId;

    @NotNull(message="{id.notNull}")
    @Positive(message = "{id.positive}")
    private Long lastRootCommentId;
    private int size;
}
