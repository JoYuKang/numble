package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 조회
    Optional<Board> findAllById(Long id);
    
    List<Board> findAllByUserId(Long userID);
    List<Board> findAllByOrderByCreatedDateDesc();


}
