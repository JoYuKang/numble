package com.project.numble.application.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RootCommentResponse {

    private Long commentId;
    private String author;
    private String address;
    private String content;
    private Long depth;
    private boolean deleted;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ChildCommentResponse> children;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "Asia/Seoul")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;

    @JsonIgnore
    private long childrenCount;

    public void setChildren(
        List<ChildCommentResponse> children) {
        this.children = children;
    }
}
