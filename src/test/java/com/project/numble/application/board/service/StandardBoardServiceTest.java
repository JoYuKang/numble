package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.dto.request.AddBoardRequest;
import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.board.dto.response.GetAllBoardResponse;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.helper.factory.entity.UserFactory;
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

        User user = UserFactory.createStaticUser();
//        userRepository.save(user);
//        userRepository.save(user);
//        AddBoardRequest request = AddBoardRequest.builder()
//                .content("test content")
//                .address(user.getAddress())
//                .user(user)
//                .build();
//        Long boardId = boardService.addBoard(request, user.getId());
//
//
//        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotExistsException());
//        log.info("content " + board.getContent());
//        log.info("nickname " + board.getUser().getNickname());
//        log.info("address " + board.getAddress().getAddressName());
//        log.info("region1 " + board.getAddress().getRegionDepth1());
//        log.info("region2 " + board.getAddress().getRegionDepth2());
    }

    @Test
    @DisplayName("수정 테스트")
    void update(){
    }

    @Test
    @DisplayName("Board 전체 조회")
    void searchAll(){

    }

    @Test
    @DisplayName("user가 쓴 board 조회")
    void searchAllByUser(){

    }


    @Test
    @DisplayName("board 삭제 후 조회 test")
    void del(){

    }

}