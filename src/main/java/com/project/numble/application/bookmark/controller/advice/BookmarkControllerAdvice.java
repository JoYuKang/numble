package com.project.numble.application.bookmark.controller.advice;

import com.project.numble.application.bookmark.controller.BookmarkController;
import com.project.numble.application.bookmark.service.exception.AlreadyBookmarkBoardException;
import com.project.numble.application.bookmark.service.exception.BookmarkNotExistsException;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.common.advice.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {BookmarkController.class})
public class BookmarkControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(AlreadyBookmarkBoardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> alreadyBookmarkBoardExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.ALREADY_BOOKMARK_BOARD_EXCEPTION);
    }

    @ExceptionHandler(BookmarkNotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> bookmarkNotExistsExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.BOOKMARK_NOT_EXIST_EXCEPTION);
    }
}
