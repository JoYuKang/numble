package com.project.numble.application.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.Animal;
import com.project.numble.application.user.domain.enums.AnimalType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    private List<String> boardAnimalTypes;

    // tag 예정
    private String categoryType;

    private String boardAddress;

    // 댓글 불러오기
    private List<GetCommentResponse> comments = new ArrayList<>();

    // 좋아요 수
    private Integer likeCount;

    @Setter
    private boolean likeCheck;

    private Integer viewCount;

    // 생성 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    // 수정 시간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private  LocalDateTime lastModifiedDate;

    public GetBoardResponse(Board board) {
        this.content = board.getContent();
        this.nickname = board.getUser().getNickname();
        this.boardAddress = board.getBoardAddress();
        this.categoryType = board.getCategoryType();
        this.comments = board.getComments().stream().map(GetCommentResponse::new).collect(Collectors.toList());
        this.boardAnimalTypes = board.getBoardAnimals().stream().map(animal -> AnimalType.getName(animal.getAnimalType())).collect(
                Collectors.toList());
        this.likeCount = board.getLikeCount();
        this.viewCount = board.getViewCount();
        this.createdDate = board.getCreatedDate();
        this.lastModifiedDate = board.getLastModifiedDate();
    }
}
