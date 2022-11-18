package com.project.numble.application.board.service;

public interface BookmarkService {

    void addBookmark(Long userId, Long boardId);

    void cancelBookmark(Long userId, Long boardId);

    boolean isNotAlreadyBookmark(Long userId, Long boardId);

}
