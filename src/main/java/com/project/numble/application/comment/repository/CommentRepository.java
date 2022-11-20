package com.project.numble.application.comment.repository;

import com.project.numble.application.comment.domain.Comment;
import com.project.numble.application.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {

    List<Comment> findAllByBoardId(Long boardId);

    List<Comment> findAllByUserOrderByCreatedDateDesc(User user);
}
