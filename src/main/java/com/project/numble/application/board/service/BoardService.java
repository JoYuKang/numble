package com.project.numble.application.board.service;

import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.user.domain.User;

import java.util.List;

public interface BoardService {

    GetBoardResponse getBoard(Long id);
    List<GetBoardResponse> getBoardList();

    Long save(AddBoardRequest addBoardRequest, Long userId);

    Long updateBoard(Long id, ModBoardRequest modBoardRequest);

    List<GetBoardResponse> getBoardUser(User user);

    void delete(Long id);

}
