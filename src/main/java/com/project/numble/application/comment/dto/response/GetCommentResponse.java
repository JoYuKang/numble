package com.project.numble.application.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.numble.application.comment.domain.Comment;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetCommentResponse {

    private Long commentId;
    private String author;
    private String content;
    private Long depth;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long parentId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long rootId;

    public static GetCommentResponse fromComment(Comment comment) {
        return GetCommentResponse.builder()
            .commentId(comment.getId())
            .author(comment.getAuthorName())
            .content(comment.getContent())
            .depth(comment.getDepth())
            .createdDate(comment.getCreatedDate())
            .parentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
            .rootId(comment.getRootComment().getId() != null ? comment.getRootComment().getId() : null)
            .build();
    }
}
