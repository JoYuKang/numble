package com.project.numble.application.board.controller;

import com.project.numble.application.board.service.StandardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final StandardCommentService commentService;

    // 미구현
}
