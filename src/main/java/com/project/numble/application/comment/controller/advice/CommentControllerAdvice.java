package com.project.numble.application.comment.controller.advice;

import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.comment.controller.CommentController;
import com.project.numble.application.comment.service.exception.CommentNotAuthException;
import com.project.numble.application.comment.service.exception.CommentNotFoundException;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.common.advice.ExceptionType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {CommentController.class})
public class CommentControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> commentNotFoundExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.COMMENT_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(CommentNotAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Map<String, String> commentNotAuthExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.COMMENT_NOT_AUTH_EXCEPTION);
    }

    @ExceptionHandler(BoardNotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> boardNotExistsExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.BOARD_NOT_EXISTS_EXCEPTION);
    }
}
