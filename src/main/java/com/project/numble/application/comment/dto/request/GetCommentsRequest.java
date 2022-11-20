package com.project.numble.application.comment.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetCommentsRequest {

    @NotNull(message="{id.notNull}")
    private Long boardId;

    @NotNull(message="{id.notNull}")
    private Long lastCommentId;
    private int size;
}
