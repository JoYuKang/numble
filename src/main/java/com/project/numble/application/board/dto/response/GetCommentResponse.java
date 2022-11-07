package com.project.numble.application.board.dto.response;

import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@RequiredArgsConstructor
public class GetCommentResponse {

    private Long id;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    private User user;

    @Builder
    GetCommentResponse(Long id, String content, User user) {
        this.id = id;
        this.content = content;
        this.user = user;
    }

    public Comment toEntity() {
        return Comment.builder()
                .user(user)
                .content(content)
                .build();
    }

    public GetCommentResponse(Comment comment) {
        this.content = comment.getContent();
        this.user = comment.getUser();
    }

}
