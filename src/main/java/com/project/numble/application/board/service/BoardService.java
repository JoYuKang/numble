package com.project.numble.application.board.service;

import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetAllBoardResponse;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.user.domain.User;

import java.util.List;

public interface BoardService {

    GetBoardResponse getBoard(Long userId, Long boardId);
    List<GetAllBoardResponse> getBoardList(Long userId);

    Long addBoard(AddBoardRequest addBoardRequest, Long userId);

    Long updateBoard(Long boardId, Long userId, ModBoardRequest modBoardRequest);

    List<GetAllBoardResponse> getBoardUser(Long userId);

    void delete(Long id);

}
