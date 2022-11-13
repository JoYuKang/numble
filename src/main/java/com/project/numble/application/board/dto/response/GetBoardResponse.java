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
    private Animal animal;

    // tag 예정
    private Category category;

    private String addressName;

    private String regionDepth1;

    private String regionDepth2;

    // 댓글 불러오기
    private List<GetCommentResponse> comments = new ArrayList<>();

    // 생성 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    // 수정 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private  LocalDateTime lastModifiedDate;

    @Builder
    GetBoardResponse(String content, User user, Address address) {
        this.content = content;
        this.nickname = user.getNickname();
//        this.address = address;
    }

    public Board toEntity() {
        return Board.builder()
                .content(content)
                .build();
    }

    public GetBoardResponse(Board board) {
        this.content = board.getContent();
        this.nickname = board.getUser().getNickname();
        this.addressName = board.getAddress().getAddressName();
        this.regionDepth1 = board.getAddress().getRegionDepth1();
        this.regionDepth2 = board.getAddress().getRegionDepth2();
        this.comments = board.getComments().stream().map(GetCommentResponse::new).collect(Collectors.toList());
        this.createdDate = board.getCreatedDate();
        this.lastModifiedDate = board.getLastModifiedDate();
    }

}
