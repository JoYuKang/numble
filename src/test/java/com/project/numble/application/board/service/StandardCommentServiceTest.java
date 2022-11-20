package com.project.numble.application.board.service;

import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.service.StandardUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
class StandardCommentServiceTest {

    @Autowired StandardCommentService commentService;
    @Autowired StandardBoardService boardService;
    @Autowired
    StandardUserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("comment save test")
    void addComment() {
    }
}