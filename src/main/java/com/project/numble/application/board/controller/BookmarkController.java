package com.project.numble.application.board.controller;

import com.project.numble.application.board.service.BookmarkService;
import com.project.numble.core.resolver.SignInUser;
import com.project.numble.core.resolver.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{boardId}")
    public ResponseEntity<Void> addBookmark(@PathVariable("boardId") Long boardId, @SignInUser UserInfo userInfo ) {
        bookmarkService.addBookmark(userInfo.getUserId(), boardId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> cancelLike(@PathVariable("boardId") Long boardId, @SignInUser UserInfo userInfo) {
        bookmarkService.cancelBookmark(userInfo.getUserId(), boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
