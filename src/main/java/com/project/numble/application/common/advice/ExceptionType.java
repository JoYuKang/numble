package com.project.numble.application.common.advice;

import lombok.Getter;

@Getter
public enum ExceptionType {
    EXCEPTION("exception.message"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("methodArgumentNotValidException.message"),
    USER_EMAIL_ALREADY_EXISTS_EXCEPTION("userEmailAlreadyExistsException.message"),
    USER_NOT_FOUND_EXCEPTION("userNotFoundException.message"),
    SIGN_IN_FAILURE_EXCEPTION("signInFailureException.message"),
    USER_NICKNAME_ALREADY_EXISTS_EXCEPTION("userNicknameAlreadyExistsException.message"),
    BOARD_NOT_EXISTS_EXCEPTION("boardNotExistsException.message"),
    URL_CONNECTION_IO_EXCEPTION("urlConnectionIOException.message"),
    COMMENT_NOT_EXISTS_EXCEPTION ("commentNotExistsException.message"),
    USER_ALREADY_SIGN_OUT_EXCEPTION("userAlreadySignOutException.message"),
    CURRENT_USER_NOT_SAME_WRITER("currentUserNotSameWriter.message"),
    COMMENT_NOT_IN_BOARD("commentNotInBoard.message"),
    WITHDRAWAL_USER_EXCEPTION("withdrawalUserException.message"),
    BOARD_ANIMALS_NOT_EXISTS_EXCEPTION("boardAnimalsNotExistsException.message"),
    ALREADY_LIKE_BOARD_EXCEPTION("alreadyLikeBoardException.message"),
    LIKE_NOT_EXISTS_EXCEPTION("likeNotExistsException.message"),
    ALREADY_BOOKMARK_BOARD_EXCEPTION("alreadyBookmarkBoardException.message"),
    BOOKMARK_NOT_EXIST_EXCEPTION("bookmarkNotExistsException.message"),
    COMMENT_NOT_FOUND_EXCEPTION("commentNotFoundException.message"),
    COMMENT_NOT_AUTH_EXCEPTION("commentNotAuthException.message");

    private final String message;

    ExceptionType(String message) {
        this.message = message;
    }
}
