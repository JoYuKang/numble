package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, CustomBoardRepository {

    // 조회
    @EntityGraph(attributePaths = {"user", "likes", "boardAnimals"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Board> findById(Long boardId);

    @EntityGraph(attributePaths = {"user", "likes", "boardAnimals"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Board> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "likes", "boardAnimals"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Board> findAllByOrderByCreatedDateDesc();

}
