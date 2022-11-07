package com.project.numble.application.board.dto.response;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Category;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.AnimalType;
import com.project.numble.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class GetBoardResponse {

    private Long id;

    private String content;

    // 이미지 예정
    private List<Image> image;

    private User user;

    // 동물 예정
    private AnimalType animalType;

    // tag 예정
    private Category category;

    // 댓글 불러오기
    private List<GetCommentResponse> comments = new ArrayList<>();

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
        this.comments = board.getComments().stream().map(GetCommentResponse::new).collect(Collectors.toList());
    }

}
