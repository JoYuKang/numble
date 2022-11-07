package com.project.numble.application.board.dto.request;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@RequiredArgsConstructor
public class ModCommentRequest {

    @NotEmpty(message = "내용은 필수입니다.")
    private String comment;

    private Board board;

    private User user;


    @Builder
    public ModCommentRequest(String comment, Board board, User user) {
        this.comment = comment;
        this.board = board;
        this.user = user;
    }
}
