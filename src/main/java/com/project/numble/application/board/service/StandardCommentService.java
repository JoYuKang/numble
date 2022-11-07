package com.project.numble.application.board.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.board.dto.request.AddCommentRequest;
import com.project.numble.application.board.dto.response.GetBoardResponse;
import com.project.numble.application.board.dto.response.GetCommentResponse;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.board.repository.CommentRepository;
import com.project.numble.application.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StandardCommentService implements CommentService{

    private final BoardService boardService;

    private final CommentRepository commentRepository;

    @Override
    public Long save(AddCommentRequest commentRequest) {
        return commentRepository.save(commentRequest.toEntity()).getId();
    }

    @Override
    public List<GetCommentResponse> getComments(Long id) {

        Board board = boardService.getBoard(id).toEntity();
        
        return commentRepository.findAllByBoard(board).stream().map(GetCommentResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<GetCommentResponse> getCommentsByUser(User user) {
        return null;
    }


}
