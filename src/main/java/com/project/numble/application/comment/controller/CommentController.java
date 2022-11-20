package com.project.numble.application.comment.controller;

import com.project.numble.application.comment.dto.request.AddCommentRequest;
import com.project.numble.application.comment.dto.request.ReplyCommentRequest;
import com.project.numble.application.comment.dto.request.ModCommentRequest;
import com.project.numble.application.comment.service.CommentService;
import com.project.numble.core.resolver.SignInUser;
import com.project.numble.core.resolver.UserInfo;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<Void> addComment(@SignInUser UserInfo userInfo, @Valid @RequestBody AddCommentRequest request) {
        commentService.addComment(userInfo, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/reply")
    public ResponseEntity replyComment(@SignInUser UserInfo userInfo, @Valid @RequestBody ReplyCommentRequest request) {
        commentService.replyContent(userInfo, request);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/modify")
    public ResponseEntity modifyComment(@SignInUser UserInfo userInfo, @Valid @RequestBody ModCommentRequest request) {
        commentService.updateComment(userInfo, request);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteComment(@SignInUser UserInfo userInfo, @PathVariable Long id) {
        commentService.deleteComment(userInfo, id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity getAllComments(@PathVariable Long boardId) {
        return new ResponseEntity(commentService.getAllComments(boardId), HttpStatus.OK);
    }

    @GetMapping("/my-comments")
    public ResponseEntity getMyComments(@SignInUser UserInfo userInfo) {
        return new ResponseEntity(commentService.getMyComments(userInfo), HttpStatus.OK);
    }
}
