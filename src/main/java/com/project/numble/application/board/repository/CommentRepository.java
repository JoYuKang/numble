package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 자신이 쓴 댓글 모아보기 기능
    List<Comment> findAllByUserOrderByCreatedDateDesc(User user);

    // 댓글 보기
    List<Comment> findAllByBoard(Board board);
}
