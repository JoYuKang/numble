package com.project.numble.application.board.dto.request;

import com.project.numble.application.board.domain.Category;
import com.project.numble.application.board.domain.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ModBoardRequest {

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    private List<Image> images;

    @NotEmpty(message = "내용은 필수입니다.")
    private List<Category> category;

    private LocalDateTime lastModifiedDate;

    @Builder
    public ModBoardRequest(String content) {
        this.content = content;
        this.lastModifiedDate = LocalDateTime.now();
    }

    @Builder
    public ModBoardRequest(String content, Image... images) {
        this.content = content;
        for (Image image : images
        ) {
            this.images.add(image);
        }
        this.lastModifiedDate = LocalDateTime.now();
    }


}
