package com.project.numble.application.like.service;

public interface LikeService {
    void addLike(Long userId, Long boardId);

    boolean isNotAlreadyLike(Long userId, Long boardId);

    void cancelLike(Long userId, Long boardId);

}
