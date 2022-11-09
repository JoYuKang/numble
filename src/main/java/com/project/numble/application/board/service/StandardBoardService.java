package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import com.project.numble.application.user.service.StandardUserService;
import com.project.numble.application.user.service.exception.UserNicknameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StandardBoardService implements BoardService{

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    // 저장
    @Transactional
    @Override
    public Long save(AddBoardRequest request) {
        Board board = request.toEntity();
        Optional<User> findUser = userRepository.findByEmail(request.getUser().getEmail());
        User user = findUser.orElseThrow(() -> new NoSuchElementException());
        user.addBoard(board);
        return boardRepository.save(board).getId();
    }


    // 단건 조회
    @Override
    @Transactional(readOnly = true)
    public GetBoardResponse getBoard(Long id) {
        Board board = getBoardOne(id);

        return GetBoardResponse.builder()
                .user(board.getUser())
                .id(board.getId())
                .content(board.getContent())
                .build();
    }



    // 자기가 작성한 글 조회
    @Override
    public List<GetBoardResponse> getBoardUser(User user) {
        return boardRepository.findAllByUser(user).stream().map(GetBoardResponse::new).collect(Collectors.toList());
    }

    // 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<GetBoardResponse> getBoardList() {
        return boardRepository.findAllByOrderByCreatedDateDesc().stream().map(GetBoardResponse::new).collect(Collectors.toList());
    }


    // 수정
    @Override
    public Long updateBoard(Long boardId, ModBoardRequest request) {

        // 개시글 가져오기
        Board board = getBoardOne(boardId);
        board.update(request.getContent());

        return boardId;
    }

    // 삭제
    @Override
    public void delete(Long id) {

        Board delBoard = boardRepository.findAllById(id).orElseThrow(() -> new BoardNotExistsException());

        userRepository.findByEmail(delBoard.getUser().getEmail()).orElseThrow(() -> new UserNotFoundException())
                        .delBoard(delBoard);

        boardRepository.deleteById(id);
    }


    // Board 가져오기
    private Board getBoardOne(Long id) {
        return boardRepository.findAllById(id).orElseThrow(() -> new BoardNotExistsException());
    }
}
