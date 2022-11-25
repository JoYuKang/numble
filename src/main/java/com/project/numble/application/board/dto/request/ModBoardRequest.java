package com.project.numble.application.board.dto.request;

import com.project.numble.application.image.domain.Image;
import com.project.numble.application.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public class ModBoardRequest {

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    private List<Long> imageIds = new ArrayList<>();

    @NotEmpty(message = "카테고리는 필수입니다.")
    private String categoryType;

    private String boardAddress;

    private Set<String> boardAnimalTypes = new HashSet<>();

}
