package com.project.numble.application.board.controller.advice;

import com.project.numble.application.board.controller.BoardController;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.board.service.exception.CurrentUserNotSameWriter;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.common.advice.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {BoardController.class})
public class BoardControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(BoardNotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> boardNotExistsExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.BOARD_NOT_EXISTS_EXCEPTION);
    }

    @ExceptionHandler(CurrentUserNotSameWriter.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> currentUserNotSameWriterExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.CURRENT_USER_NOT_SAME_WRITER);
    }


}
