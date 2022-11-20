package com.project.numble.application.comment.repository;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.comment.domain.Comment;
import com.project.numble.application.comment.dto.response.ChildCommentResponse;
import com.project.numble.application.comment.dto.response.RootCommentResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface CustomCommentRepository {

    List<Comment> findCommentsOrderByHierarchy(Pageable pageable, Board board);

    Optional<Comment> findByIdWithAuthor(Long id);

    Optional<Comment> findByIdWithRootComment(Long id);

    Optional<Comment> findByIdWithRootCommentAndAuthor(Long id);

    void adjustHierarchyOrders(Comment newComment);

    void deleteChildComments(Comment parentComment);

    Long countCommentsByBoard(Board board);

    void deleteAllByBoard(Board board);

    List<RootCommentResponse> findCommentsWithChildrenCount(Pageable pageable, Board board);

    List<ChildCommentResponse> findChildrenCommentsByRootCommentId(Board board, Long rootCommentId, Long commentId);

    List<RootCommentResponse> findRootComments(Board board);

    List<ChildCommentResponse> findChildrenCommentsByRootId(Board board, Long rootCommentId);
}
