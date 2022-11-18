package com.project.numble.application.board.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.board.domain.Like;
import com.project.numble.application.user.domain.Animal;
import com.project.numble.application.user.domain.enums.AnimalType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class GetAllBoardResponse {

    private Long boardId;
    private String content;

    // 이미지 예정
    private List<Image> image;

    private String nickname;

    // 동물 예정
    private List<String> boardAnimalTypes;

    private String categoryType;

    private String boardAddress;

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

    public GetAllBoardResponse(Board board) {
        this.boardId = board.getId();
        this.content = board.getContent();
        this.nickname = board.getUser().getNickname();
        this.boardAddress = board.getBoardAddress();
        this.categoryType = board.getCategoryType();
        this.createdDate = board.getCreatedDate();
        this.boardAnimalTypes = board.getBoardAnimals().stream().map(animal -> AnimalType.getName(animal.getAnimalType())).collect(
                Collectors.toList());
        this.likeCount = board.getLikeCount();
        this.viewCount = board.getViewCount();
        this.lastModifiedDate = board.getLastModifiedDate();
    }

}
