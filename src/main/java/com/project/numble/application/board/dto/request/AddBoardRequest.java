package com.project.numble.application.board.dto.request;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @NotEmpty(message = "동물은 필수입니다.")
    private Set<String> boardAnimalTypes = new HashSet<>();

}
