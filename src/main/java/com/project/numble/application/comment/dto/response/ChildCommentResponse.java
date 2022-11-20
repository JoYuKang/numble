package com.project.numble.application.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ChildCommentResponse {

    private Long commentId;
    private String author;
    private String content;
    private Long depth;
    private String address;
    private boolean deleted;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;

    private Long rootId;

    private Long parentId;
    private String parentAuthor;
}
