package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndBoardId(Long userId, Long boardId);

}
