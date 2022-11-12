package com.project.numble.application.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Category;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.Address;
import com.project.numble.application.user.domain.Animal;
import com.project.numble.application.user.domain.User;
import io.netty.channel.local.LocalAddress;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetAllBoardResponse {

    private String content;

    // 이미지 예정
    private List<Image> image;

    private String nickname;

    // 동물 예정
    private Animal animal;

    // tag 예정
    private Category category;

    private String addressName;

    private String regionDepth1;

    private String regionDepth2;

    // 생성 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    // 수정 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private  LocalDateTime lastModifiedDate;

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
        this.addressName = board.getAddress().getAddressName();
        this.regionDepth1 = board.getAddress().getRegionDepth1();
        this.regionDepth2 = board.getAddress().getRegionDepth2();
        this.createdDate = board.getCreatedDate();
        this.lastModifiedDate = board.getLastModifiedDate();
    }

}
