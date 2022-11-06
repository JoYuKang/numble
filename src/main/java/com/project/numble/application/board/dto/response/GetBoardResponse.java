package com.project.numble.application.board.dto.response;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Category;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GetBoardResponse {

    private Long id;

    private String content;

    // 추가 예정
    private Image image;

    private User user;

    private Category category;

    @Builder
    GetBoardResponse(Long id, String content, User user) {
        this.id = id;
        this.content = content;
        this.user = user;
    }

    public Board toEntity() {
        return Board.builder()
                .user(user)
                .content(content)
                .build();
    }

    public GetBoardResponse(Board board) {
        this.content = board.getContent();
        this.user = board.getUser();
    }

}
