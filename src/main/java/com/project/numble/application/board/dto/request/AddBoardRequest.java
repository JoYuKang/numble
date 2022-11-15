package com.project.numble.application.board.dto.request;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AddBoardRequest {

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;
    private User user;

    private List<Image> imageList;

    @NotEmpty(message = "카테고리는 필수입니다.")
    private String categoryType;

    private String boardAddress;

    private List<String> boardAnimalTypes;

    public Board toEntity() {
        return Board.builder()
                .content(content)
                .user(user)
                .categoryType(categoryType)
                .boardAddress(boardAddress)
                .build();
    }

}
