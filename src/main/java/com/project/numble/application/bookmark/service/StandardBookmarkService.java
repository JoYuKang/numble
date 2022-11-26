package com.project.numble.application.bookmark.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.bookmark.domain.Bookmark;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.bookmark.repository.BookmarkRepository;
import com.project.numble.application.bookmark.service.exception.AlreadyBookmarkBoardException;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.bookmark.service.exception.BookmarkNotExistsException;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StandardBookmarkService implements BookmarkService{

    private final BookmarkRepository bookmarkRepository;

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    @Override
    public void addBookmark(Long userId, Long boardId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotExistsException());
        Bookmark bookmark = new Bookmark(user, board);
        if (isNotAlreadyBookmark(userId, boardId)) {
            bookmarkRepository.save(bookmark);
            board.addBookmark(bookmark);
            board.plusBookmarkCount();
            return;
        }
        throw new AlreadyBookmarkBoardException();
    }

    @Override
    public void cancelBookmark(Long userId, Long boardId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndBoardId(userId, boardId).orElseThrow(() -> new BookmarkNotExistsException());
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotExistsException());
        bookmarkRepository.delete(bookmark);
        board.minusBookmarkCount();
        board.delBookmark(bookmark);
    }

    @Override
    public boolean isNotAlreadyBookmark(Long userId, Long boardId) {
        return bookmarkRepository.findByUserIdAndBoardId(userId, boardId).isEmpty();
    }
}
