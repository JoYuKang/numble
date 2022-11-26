package com.project.numble.application.like.controller;

import com.project.numble.application.like.service.LikeService;
import com.project.numble.core.resolver.SignInUser;
import com.project.numble.core.resolver.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{boardId}")
    public ResponseEntity<Void> addLike(@PathVariable("boardId") Long boardId, @SignInUser UserInfo userInfo) {

        likeService.addLike(userInfo.getUserId(), boardId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> cancelLike(@PathVariable("boardId") Long boardId, @SignInUser UserInfo userInfo) {
        likeService.cancelLike(userInfo.getUserId(), boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
