package com.project.numble.application.comment.service;

import com.project.numble.application.comment.dto.request.ChildrenCommentsRequest;
import com.project.numble.application.comment.dto.request.AddCommentRequest;
import com.project.numble.application.comment.dto.request.ReplyCommentRequest;
import com.project.numble.application.comment.dto.request.ModCommentRequest;
import com.project.numble.application.comment.dto.response.ChildrenCommentsResponse;
import com.project.numble.application.comment.dto.response.GetCommentResponse;
import com.project.numble.application.comment.dto.response.GetCommentsResponse;
import com.project.numble.application.comment.dto.response.RootCommentResponse;
import com.project.numble.application.comment.dto.response.RootsCommentsResponse;
import com.project.numble.core.resolver.UserInfo;
import java.util.List;
import org.springframework.data.domain.PageRequest;

public interface CommentService {

    void addComment(UserInfo userInfo, AddCommentRequest request);

    void replyContent(UserInfo userInfo, ReplyCommentRequest request);

    GetCommentsResponse getComments(UserInfo userInfo, Long postId, PageRequest pageRequest);

    RootsCommentsResponse getRootComments(UserInfo userInfo, Long postId, PageRequest pageRequest);

    ChildrenCommentsResponse getChildrenComments(UserInfo userInfo, ChildrenCommentsRequest request);

    void updateComment(UserInfo userInfo, ModCommentRequest request);

    void deleteComment(UserInfo userInfo, Long commentId);

    List<RootCommentResponse> getAllComments(Long boardId);

    List<GetCommentResponse> getMyComments(UserInfo userInfo);
}
