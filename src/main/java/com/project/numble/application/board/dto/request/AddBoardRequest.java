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
import java.util.Date;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AddBoardRequest {

    private Long id;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;
    private User user;

    private List<Image> imageList;

    private LocalDateTime createdDate;

    @Builder
    public AddBoardRequest(User user, String content) {
        this.user = user;
        this.content = content;
        this.createdDate = LocalDateTime.now();
    }

    @Builder
    public AddBoardRequest(User user, String content,Image... images) {
        this.user = user;
        this.content = content;
        for (Image image : images) {
            this.imageList.add(image);
        }
        this.createdDate = LocalDateTime.now();
    }


    public Board toEntity() {
        return Board.builder()
                .user(user)
                .content(content)
                .build();
    }

}
