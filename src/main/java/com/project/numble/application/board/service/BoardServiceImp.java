package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.exception.BoardNotExistsException;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImp implements BoardService{

    private final String DAILY_ROUTINE = "일상";
    private final String INFORMATION = "정보";

    private final BoardRepository boardRepository;

    // 저장
    @Transactional
    @Override
    public Long save(AddBoardRequest request) {
        AddBoardRequest boardRequest = AddBoardRequest.builder()
                .user(request.getUser())
                .content(request.getContent())
                .build();
        return boardRepository.save(boardRequest.toEntity()).getId();
    }


    // 단건 조회
    @Override
    @Transactional(readOnly = true)
    public GetBoardResponse getBoard(Long id) {
        Board board = getBoardOne(id);

        GetBoardResponse boardResponse = GetBoardResponse.builder()
                .user(board.getUser())
                .id(board.getId())
                .content(board.getContent())
                .build();

        return boardResponse;
    }



    // 자기가 작성한 글 조회
    @Override
    public List<GetBoardResponse> getBoardUser(User user) {
        List<Board> boards = new ArrayList<>();

        //boardRepository.findAllByUser(user);

        return null;
    }


    // 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<GetBoardResponse> getBoardList() {
        List<Board> boards = boardRepository.findAllByOrderByCreatedDateDesc();

        return boards.stream().map(GetBoardResponse::new).collect(Collectors.toList());
    }


    // 수정
    @Override
    public Long updateBoard(Long id, ModBoardRequest request) {
        ModBoardRequest boardRequest = ModBoardRequest.builder()
                .content(request.getContent())
                .build();

        // 개시글 가져오기
        Board board = getBoardOne(id);

        // 수정일자 누락 확인하기
        board.update(boardRequest.getContent());

        return id;
    }

    // Board 가져오기
    private Board getBoardOne(Long id) {
        Optional<Board> boardWrapper = boardRepository.findAllById(id);
        Board board = boardWrapper.orElseThrow(()
                -> new BoardNotExistsException());

        return board;
    }
}
