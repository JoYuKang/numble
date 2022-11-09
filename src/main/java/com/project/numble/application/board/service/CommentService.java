package com.project.numble.application.board.service;

import com.project.numble.application.board.dto.request.AddCommentRequest;
import com.project.numble.application.board.dto.request.ModCommentRequest;
import com.project.numble.application.board.dto.response.GetCommentResponse;

import java.util.List;

public interface CommentService {

    Long save(Long boardId, AddCommentRequest commentRequest);

    List<GetCommentResponse> getComments(Long boardId);

    List<GetCommentResponse> getCommentsByUser(Long userId);

    Long updateComment(Long id, ModCommentRequest commentRequest);

    void delete(Long commentId);


}
