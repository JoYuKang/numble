package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.board.dto.request.AddCommentRequest;
import com.project.numble.application.board.dto.response.GetCommentResponse;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.board.repository.CommentRepository;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.service.StandardUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void save() {
        User user = User.createNormalUser("test@test.com", "1234!", "testUser");
        userRepository.save(user);
        Board board = Board.builder().user(user).content("test content for comment").build();
        boardRepository.save(board);
        AddCommentRequest addCommentRequest1 = new AddCommentRequest("comment content1", board, user);
        Long saveCommentId1 = commentService.save(board.getId(), user.getId(), addCommentRequest1);

        AddCommentRequest addCommentRequest2 = new AddCommentRequest("comment content2", board, user);
        Long saveCommentId2 = commentService.save(board.getId(), user.getId(), addCommentRequest2);

        log.info("comment get id = " + saveCommentId1);
        log.info("comment get id = " + saveCommentId2);

        List<Comment> all = commentRepository.findAll();
        for (Comment comment : all) {
            log.info("comment id = " + comment.getId());
            log.info("comment boardId= " + comment.getBoard().getId());
            log.info("comment board content = " + comment.getBoard().getContent());
            log.info("comment content = " + comment.getContent());
        }


    }
}