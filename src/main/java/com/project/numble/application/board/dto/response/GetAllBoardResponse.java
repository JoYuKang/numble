package com.project.numble.application.board.dto.response;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Category;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.User;
import io.netty.channel.local.LocalAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetAllBoardResponse {

    private String content;

    // 이미지 예정
    private List<Image> image;

    private String nickname;

    // 동물 예정
    private AnimalType animalType;

    // tag 예정
    private Category category;

    // 생성 시간
    private LocalAddress createdDate;

    @Builder
    GetAllBoardResponse(String content, User user) {
        this.content = content;
        this.nickname = user.getNickname();
    }

    public Board toEntity() {
        return Board.builder()
                .content(content)
                .build();
    }

    public GetAllBoardResponse(Board board) {
        this.content = board.getContent();
        this.nickname = board.getUser().getNickname();
    }

}
