package com.project.numble.application.board.service;

import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetAllBoardResponse;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.user.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardService {

    GetBoardResponse getBoard(Long userId, Long boardId);

    // 자기가 작성한 글 조회
    List<GetAllBoardResponse> getBoardUser(Long userId, Long lastBoardId);

    List<GetAllBoardResponse> getBoardList(Long userId, String address, String animal, String category, Long lastBoardId);

    Long addBoard(AddBoardRequest addBoardRequest, Long userId);

    Long updateBoard(Long boardId, Long userId, ModBoardRequest modBoardRequest);

    List<GetAllBoardResponse> getBookmarkBoard(Long userId, Long lastBoardId);
    void delete(Long id);

}
