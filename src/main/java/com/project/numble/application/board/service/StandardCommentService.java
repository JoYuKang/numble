package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.board.dto.request.AddCommentRequest;
import com.project.numble.application.board.dto.request.ModCommentRequest;
import com.project.numble.application.board.dto.response.GetCommentResponse;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.board.repository.CommentRepository;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.board.service.exception.CommentNotExistsException;
import com.project.numble.application.board.service.exception.CurrentUserNotSameCommentUser;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StandardCommentService implements CommentService{

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    @Override
    public Long save(Long boardId, Long userId, AddCommentRequest request) {
        Optional<Board> optionalBoard = boardRepository.findAllById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new BoardNotExistsException());

        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());

        Comment comment = request.toEntity();

        comment.setUser(user);
        comment.setBoard(board);
        return commentRepository.save(comment).getId();
    }

    @Override
    public List<GetCommentResponse> getComments(Long boardId) {

        Optional<Board> optionalBoard = boardRepository.findAllById(boardId);
        Board board = optionalBoard.orElseThrow(() -> new BoardNotExistsException());

        return commentRepository.findAllByBoard(board).stream().map(GetCommentResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<GetCommentResponse> getCommentsByUser(Long userId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());
        return commentRepository.findAllByUserOrderByCreatedDateDesc(user).stream().map(GetCommentResponse::new).collect(Collectors.toList());
    }

    @Override
    public Long updateComment(Long boardId, Long userId, ModCommentRequest request) {
        // board, user 존재 여부 조회 없다면 Exception
        String currentUserNickname = checkBoardAndUser(boardId, userId);
        // 댓글 가져오기
        Comment comment = getComment(request.getId());
        // 현재 사용자와 댓글 작성자 비교
        checkUser(currentUserNickname, comment);

        comment.update(request.getContent());
        return comment.getId();
    }

    @Override
    public void delete(Long commentId, Long userId, Long boardId) {
        // board, user 존재 여부 조회 없다면 Exception
        String currentUserNickname = checkBoardAndUser(boardId, userId);
        // 댓글 가져오기
        Comment comment = getComment(commentId);
        // 현재 사용자와 댓글 작성자 비교
        checkUser(currentUserNickname, comment);
        commentRepository.delete(comment);
    }

    private Comment getComment(Long request) {
        return commentRepository.findById(request).orElseThrow(() -> new CommentNotExistsException());
    }

    private String checkBoardAndUser(Long boardId, Long userId) {
        boardRepository.findAllById(boardId).orElseThrow(() -> new BoardNotExistsException());
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException()).getNickname();
    }

    private static void checkUser(String currentUserNickname, Comment comment) {
        if (!currentUserNickname.equals(comment.getUser().getNickname())) {
            throw new CurrentUserNotSameCommentUser();
        }
    }
}
