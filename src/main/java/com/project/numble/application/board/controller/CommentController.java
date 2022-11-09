package com.project.numble.application.board.controller;

import com.project.numble.application.board.dto.request.AddCommentRequest;
import com.project.numble.application.board.service.StandardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final StandardCommentService commentService;

    @GetMapping("/board/{id}/add")
    public ResponseEntity<Long> addComment(@PathVariable("id") Long id, @Valid @RequestBody AddCommentRequest request) {
        Long saveId = commentService.save(id, request);
        return new ResponseEntity<>(saveId, HttpStatus.OK);
    }
}
