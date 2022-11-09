package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.board.dto.request.AddCommentRequest;
import com.project.numble.application.board.dto.request.ModCommentRequest;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.dto.response.GetCommentResponse;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StandardCommentService implements CommentService{

    private final StandardBoardService boardService;

    private final CommentRepository commentRepository;

    @Override
    public Long save(Long boardId, AddCommentRequest request) {
        Board board = boardService.getBoard(boardId).toEntity();
        board.addComment(request.toEntity());
        return commentRepository.save(request.toEntity()).getId();
    }

    @Override
    public List<GetCommentResponse> getComments(Long BoardId) {

        Board board = boardService.getBoard(BoardId).toEntity();
        
        return commentRepository.findAllByBoard(board).stream().map(GetCommentResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<GetCommentResponse> getCommentsByUser(Long userId) {
        return commentRepository.findAllByUserOrderByIdDesc(userId).stream().map(GetCommentResponse::new).collect(Collectors.toList());
    }

    @Override
    public Long updateComment(Long id, ModCommentRequest commentRequest) {

        return null;
    }

    @Override
    public void delete(Long commentId) {

    }


}
