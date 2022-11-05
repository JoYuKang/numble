package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
@Transactional
class BoardServiceImpTest {


    @Autowired
    BoardServiceImp boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    @DisplayName("board 저장 테스트")
    void save() {
        User user = User.createNormalUser("use@test.com", "1234", "test!");

        userRepository.save(user);

        AddBoardRequest boardRequest = AddBoardRequest.builder()
                .user(user)
                .content("testContent").build();

        Long saveId = boardService.save(boardRequest);

        Optional<User> userByEmail = userRepository.findByEmail("use@test.com");

        User findUser = userByEmail.get();

        GetBoardResponse getBoardRequest = boardService.getBoard(saveId);

        Board board = getBoardRequest.toEntity();


        log.info("boardContent : " + board.getContent());
        log.info("board user nickname : " +board.getUser().getNickname());

        Assertions.assertThat(board.getContent()).isEqualTo("testContent");
        Assertions.assertThat(board.getUser().getId()).isEqualTo(user.getId());

    }

    @Test
    @DisplayName("수정 테스트")
    void update(){
        User user = User.createNormalUser("use@test.com", "1234", "test!");

        AddBoardRequest boardRequest = AddBoardRequest.builder()
                .user(user)
                .content("before test content").build();

        Long saveId = boardService.save(boardRequest);

        String updateText = "after test content";

        GetBoardResponse getBeforeBoardRequest = boardService.getBoard(saveId);
        Board beforeBoard = getBeforeBoardRequest.toEntity();

        log.info("수정 전 content : " + beforeBoard.getContent());

        ModBoardRequest modBoardRequest = ModBoardRequest.builder()
                .content(updateText)
                .build();

        Long afterId = boardService.updateBoard(saveId, modBoardRequest);

        GetBoardResponse getAfterBoardRequest = boardService.getBoard(afterId);
        Board afterBoard = getAfterBoardRequest.toEntity();

        log.info("수정 후 content : " + afterBoard.getContent());

        Assertions.assertThat(beforeBoard.getId()).isEqualTo(afterBoard.getId());
        Assertions.assertThat(beforeBoard.getUser().getEmail()).isEqualTo(afterBoard.getUser().getEmail());
    }

    @Test
    @DisplayName("Board 전체 조회")
    void searchAll(){

        User user1 = User.createNormalUser("use1@test.com", "1234", "test1!!");

        AddBoardRequest boardRequest1 = AddBoardRequest.builder()
                .user(user1)
                .content("test content 111111").build();

        Long saveId1 = boardService.save(boardRequest1);

        User user2 = User.createNormalUser("use2@test.com", "1234", "test2@@");

        AddBoardRequest boardRequest2 = AddBoardRequest.builder()
                .user(user2)
                .content("test content 222222").build();

        Long saveId2 = boardService.save(boardRequest2);

        List<GetBoardResponse> boardList = boardService.getBoardList();

        for (GetBoardResponse board: boardList) {

            log.info(">>>> "+String.valueOf(board.getContent()));
        }

        Assertions.assertThat(boardList.size()).isEqualTo(2);

    }

}