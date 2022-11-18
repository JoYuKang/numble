package com.project.numble.application.board.service;

public interface LikeService {
    void addLike(Long userId, Long boardId);

    boolean isNotAlreadyLike(Long userId, Long boardId);

    void cancelLike(Long userId, Long boardId);

}
