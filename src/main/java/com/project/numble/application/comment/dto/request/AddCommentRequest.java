package com.project.numble.application.comment.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class AddCommentRequest {

    @NotNull(message="{id.notNull}")
    @Positive(message = "{id.positive}")
    private Long boardId;

    @NotBlank(message = "{content.notBlank}")
    private String content;
}
