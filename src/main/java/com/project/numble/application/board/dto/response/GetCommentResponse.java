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

    Long commentId;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    private String nickname;

    @Builder
    GetCommentResponse(Long commentId, String content, String nickname) {
        this.commentId = commentId;
        this.content = content;
        this.nickname = nickname;
    }


    public GetCommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getUser().getNickname();
    }

}
