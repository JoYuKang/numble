package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.Board;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomBoardRepository {

    List<Board> findBoardsOrderByIdDesc(Pageable pageable, String categoryType, String animalType, String address);

    List<Board> findUserBoardsOrderByIdDesc(Pageable pageable, Long userId);

    List<Board> findBookmarkBoardOrderByIdDesc(Pageable pageable, Long userId);


}
