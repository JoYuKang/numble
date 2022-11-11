package com.project.numble.application.board.controller.advice;

import com.project.numble.application.board.controller.CommentController;
import com.project.numble.application.board.service.exception.CommentNotExistsException;
import com.project.numble.application.board.service.exception.CurrentUserNotSameCommentUser;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.common.advice.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {CommentController.class})
public class CommentControllerAdvice {

    private final ControllerAdviceUtils utils;

    @ExceptionHandler(CommentNotExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> commentNotExistsExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.COMMENT_NOT_EXISTS_EXCEPTION);
    }

    @ExceptionHandler(CurrentUserNotSameCommentUser.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> currentUserNotSameCommentUserExceptionHandler() {
        return utils.getFailureResponse(ExceptionType.CURRENT_USER_NOT_SAME_COMMENT_USER);
    }
}
