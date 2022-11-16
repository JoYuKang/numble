package com.project.numble.application.board.controller.advice;

import com.project.numble.application.board.controller.LikeController;
import com.project.numble.application.board.service.exception.AlreadyLikeBoardException;
import com.project.numble.application.board.service.exception.CommentNotExistsException;
import com.project.numble.application.board.service.exception.LikeNotExistsException;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.common.advice.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {LikeController.class})
public class LikeControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(AlreadyLikeBoardException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> alreadyLikeBoardExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.ALREADY_LIKE_BOARD_EXCEPTION);
    }

    @ExceptionHandler(LikeNotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> likeNotExistsExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.LIKE_NOT_EXISTS_EXCEPTION);
    }
}
