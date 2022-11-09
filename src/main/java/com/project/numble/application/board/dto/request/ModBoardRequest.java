package com.project.numble.application.board.dto.request;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Category;
import com.project.numble.application.board.domain.Image;
import com.project.numble.application.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ModBoardRequest {

    Long id;
    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    private List<Image> images;

    @NotEmpty(message = "카테고리는 필수입니다.")
    private List<Category> category;

    private User user;
    @Builder
    public ModBoardRequest(Long id,String content, User user) {
        this.id = id;
        this.content = content;
        this.user = user;
    }

    @Builder
    public ModBoardRequest(String content, Image... images) {
        this.content = content;
        for (Image image : images
        ) {
            this.images.add(image);
        }
    }
    public Board toEntity() {
        return Board.builder()
                .user(user)
                .content(content)
                .build();
    }


}
