package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.Board;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomBoardRepository {

    List<Board> findBoardsOrderByIdDesc(String address, String animalType, String categoryType, Long lastBoardId);

    List<Board> findUserBoardsOrderByIdDesc(Long userId, Long lastBoardId);

    List<Board> findBookmarkBoardOrderByIdDesc(Long userId, Long lastBoardId);


}
