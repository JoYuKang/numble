package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.service.StandardUserService;
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
class StandardBoardServiceTest {


    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    StandardUserService userService;


    @Test
    @DisplayName("board 저장 테스트")
    void save() {
        User user = getUser("use@test.com", "test!");

        AddBoardRequest boardRequest = getAddBoardRequest(user, "testContent");

        Long saveId = boardService.save(boardRequest, user.getId());

        Optional<User> userByEmail = userRepository.findByEmail("use@test.com");

        User findUser = userByEmail.get();

        GetBoardResponse getBoardRequest = boardService.getBoard(saveId);

        Board board = getBoardRequest.toEntity();


        log.info("user email :" + findUser.getEmail());
        log.info("user nickname:" + findUser.getNickname());
        log.info("boardContent : " + board.getContent());
        log.info("board null check : " + board.getUser());

        Assertions.assertThat(board.getContent()).isEqualTo("testContent");
    }

    @Test
    @DisplayName("수정 테스트")
    void update(){
        User user = User.createNormalUser("use@test.com", "1234", "test!");

        AddBoardRequest boardRequest = getAddBoardRequest(user, "before test content");

        userRepository.save(user);
        Long saveId = boardService.save(boardRequest, user.getId());

        String updateText = "after test content";

        GetBoardResponse getBeforeBoardRequest = boardService.getBoard(saveId);
        Optional<Board> optionalBoard1 = boardRepository.findAllById(saveId);
        Board beforeBoard = optionalBoard1.orElseThrow(() -> new BoardNotExistsException());

        log.info("beforeBoard.getUser().getNickname() : " + beforeBoard.getUser().getNickname());
        log.info("수정 전 content : " + beforeBoard.getContent());

        ModBoardRequest modBoardRequest = ModBoardRequest.builder()
                .content(updateText)
                .build();

        Long afterId = boardService.updateBoard(saveId, modBoardRequest);

        Optional<Board> optionalBoard2 = boardRepository.findAllById(afterId);
        Board afterBoard = optionalBoard2.orElseThrow(() -> new BoardNotExistsException());

        log.info("afterBoard.getUser().getNickname() : " + afterBoard.getUser().getNickname());
        log.info("수정 후 content : " + afterBoard.getContent());


        Assertions.assertThat(beforeBoard.getUser().getNickname()).isEqualTo(afterBoard.getUser().getNickname());
    }

    @Test
    @DisplayName("Board 전체 조회")
    void searchAll(){

        User user1 = User.createNormalUser("use1@test.com", "1234", "test1!!");

        AddBoardRequest boardRequest1 = getAddBoardRequest(user1, "test content 111111");

        userRepository.save(user1);
        Long saveId1 = boardService.save(boardRequest1, user1.getId());

        User user2 = User.createNormalUser("use2@test.com", "1234", "test2@@");

        AddBoardRequest boardRequest2 = getAddBoardRequest(user2, "test content 222222");

        userRepository.save(user2);
        Long saveId2 = boardService.save(boardRequest2, user1.getId());

        List<GetBoardResponse> boardList = boardService.getBoardList();

        for (GetBoardResponse board: boardList) {
            log.info(">>>> "+String.valueOf(board.getNickname()));
            log.info(">>>> "+String.valueOf(board.getContent()));
        }

        Assertions.assertThat(boardList.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("user가 쓴 board 조회")
    void searchAllByUser(){
        User user1 = getUser("use1@test.com", "test1!!");

        AddBoardRequest boardRequest1 = getAddBoardRequest(user1, "same user 111111");
        boardService.save(boardRequest1, user1.getId());

        AddBoardRequest boardRequest2 = getAddBoardRequest(user1, "same user 222222");
        boardService.save(boardRequest2, user1.getId());

        User user2 = getUser("use2@test.com", "test2@@");

        AddBoardRequest boardRequest3 = getAddBoardRequest(user2, "other user");

        userRepository.save(user2);
        boardService.save(boardRequest3, user2.getId());

        List<Board> all = boardRepository.findAll();

        log.info("================ all ================");
        for (Board board : all) {
            log.info("board.getContent() " + board.getContent());
        }

        List<GetBoardResponse> boardUser = boardService.getBoardUser(user1);
        log.info("================ user1 ================");
        for (GetBoardResponse getBoardResponse : boardUser) {
            log.info("boardUser getNickname" + getBoardResponse.getNickname());
            log.info("boardUser getContent" + getBoardResponse.getContent());
        }

        log.info("==================================");
        log.info("board size = " + boardUser.size());
        List<Board> boards = user1.getBoards();
        for (Board board1 : boards) {
            log.info(">>> board user nickname" + board1.getUser().getNickname());
            log.info(">>> board content" + board1.getContent());
        }

        Assertions.assertThat(boardUser.size()).isEqualTo(2);

    }


    @Test
    @DisplayName("board 삭제 후 조회 test")
    void del(){
        User user1 = getUser("use1@test.com", "test1!!");

        AddBoardRequest boardRequest1 = getAddBoardRequest(user1, "same user 111111");
        Long DelId = boardService.save(boardRequest1, user1.getId());

        AddBoardRequest boardRequest2 = getAddBoardRequest(user1, "same user 222222");
        boardService.save(boardRequest2, user1.getId());

        List<GetBoardResponse> boardUser = boardService.getBoardUser(user1);

        log.info("============== board list ==============");
        for (GetBoardResponse getBoardResponse : boardUser) {
            log.info("getBoardResponse getNickname " + getBoardResponse.getNickname());
            log.info("getBoardResponse getContent " + getBoardResponse.getContent());
        }

        log.info("========= before user board list =========");
        List<Board> beforeUserBoards = user1.getBoards();
        for (Board board : beforeUserBoards) {
            log.info("board.getContent() " + board.getContent());
        }

        Assertions.assertThat(beforeUserBoards.size()).isEqualTo(2);

        boardService.delete(DelId);

        List<Board> allBoard = boardRepository.findAll();
        log.info("========= after board list =========");
        for (Board board : allBoard) {
            log.info("board.getNickname() " + board.getUser().getNickname());
            log.info("board.getContent() " + board.getContent());
        }
        log.info("========= after user board list =========");
        List<Board> afterUserBoards = user1.getBoards();
        for (Board board : afterUserBoards) {
            log.info("board.getContent() " + board.getContent());
        }


        Assertions.assertThat(afterUserBoards.size()).isEqualTo(1);

    }

    // AddBoardRequest 생성
    private static AddBoardRequest getAddBoardRequest(User user1, String content) {
        AddBoardRequest boardRequest1 = AddBoardRequest.builder()
                .user(user1)
                .content(content).build();
        return boardRequest1;
    }

    // user 생성
    private User getUser(String email, String nickname) {
        User user = User.createNormalUser(email, "1234", nickname);
        userRepository.save(user);
        return user;
    }
}