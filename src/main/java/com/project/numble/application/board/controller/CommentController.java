package com.project.numble.application.board.controller;

import com.project.numble.application.board.dto.request.AddCommentRequest;
import com.project.numble.application.board.dto.request.ModCommentRequest;
import com.project.numble.application.board.dto.response.GetCommentResponse;
import com.project.numble.application.board.service.CommentService;
import com.project.numble.application.board.service.StandardCommentService;
import com.project.numble.core.resolver.SignInUser;
import com.project.numble.core.resolver.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/board/{id}/add")
    public ResponseEntity<Long> addComment(@PathVariable("id") Long boardId, @SignInUser UserInfo userInfo, @Valid @RequestBody AddCommentRequest request) {
        Long saveId = commentService.save(boardId, userInfo.getUserId(), request);
        return new ResponseEntity<>(saveId, HttpStatus.CREATED);
    }

    @GetMapping("/comment/list")
    public ResponseEntity<List<GetCommentResponse>> commentList(@SignInUser UserInfo userInfo) {
        List<GetCommentResponse> commentsByUser = commentService.getCommentsByUser(userInfo.getUserId());
        return new ResponseEntity<>(commentsByUser, HttpStatus.OK);
    }

    @PostMapping("/board/{id}/modify")
    public ResponseEntity<Long> updateComment(@PathVariable("id") Long boardId, @SignInUser UserInfo userInfo, @Valid @RequestBody ModCommentRequest request) {
        Long updateCommentId = commentService.updateComment(boardId, userInfo.getUserId(), request);
        return new ResponseEntity<>(updateCommentId, HttpStatus.OK);
    }
    @DeleteMapping("/board/{id}/del/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId, @SignInUser UserInfo userInfo, @PathVariable("id") Long id) {
        commentService.delete(commentId, userInfo.getUserId(), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
