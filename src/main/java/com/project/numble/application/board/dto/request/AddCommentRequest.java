package com.project.numble.application.board.dto.request;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@RequiredArgsConstructor
public class AddCommentRequest {

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    private Board board;

    private User user;


    @Builder
    public AddCommentRequest(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
    }

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .board(board)
                .user(user)
                .build();
    }


}
