package com.project.numble.application.board.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@RequiredArgsConstructor
public class ModCommentRequest {

    Long id;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    @Builder
    public ModCommentRequest(String content) {
        this.content = content;
    }
}
