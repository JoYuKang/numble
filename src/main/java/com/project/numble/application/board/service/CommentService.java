package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.board.dto.request.AddCommentRequest;
import com.project.numble.application.board.dto.response.GetCommentResponse;
import com.project.numble.application.user.domain.User;

import java.util.List;

public interface CommentService {

    Long save(AddCommentRequest commentRequest);

    List<GetCommentResponse> getComments(Long id);

    List<GetCommentResponse> getCommentsByUser(User user);


}
