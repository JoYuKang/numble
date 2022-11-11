package com.project.numble.application.board.dto.response;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Category;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.User;
import io.netty.channel.local.LocalAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class GetBoardResponse {

    private String content;

    // 이미지 예정
    private List<Image> image;

    private String nickname;

    // 동물 예정
    private AnimalType animalType;

    // tag 예정
    private Category category;

    // 댓글 불러오기
    private List<GetCommentResponse> comments = new ArrayList<>();

    // 생성 시간
    private LocalAddress createdDate;

    @Builder
    GetBoardResponse(String content, User user) {
        this.content = content;
        this.nickname = user.getNickname();
    }

    public Board toEntity() {
        return Board.builder()
                .content(content)
                .build();
    }

    public GetBoardResponse(Board board) {
        this.content = board.getContent();
        this.nickname = board.getUser().getNickname();
        this.comments = board.getComments().stream().map(GetCommentResponse::new).collect(Collectors.toList());
    }

}
