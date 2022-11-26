package com.project.numble.application.comment.service;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.repository.BoardRepository;
import com.project.numble.application.board.service.exception.BoardNotExistsException;
import com.project.numble.application.board.service.exception.CommentNotExistsException;
import com.project.numble.application.comment.domain.Comment;
import com.project.numble.application.comment.dto.request.ChildrenCommentsRequest;
import com.project.numble.application.comment.dto.request.AddCommentRequest;
import com.project.numble.application.comment.dto.request.ReplyCommentRequest;
import com.project.numble.application.comment.dto.request.ModCommentRequest;
import com.project.numble.application.comment.dto.response.ChildCommentResponse;
import com.project.numble.application.comment.dto.response.ChildrenCommentsResponse;
import com.project.numble.application.comment.dto.response.GetCommentResponse;
import com.project.numble.application.comment.dto.response.GetCommentsResponse;
import com.project.numble.application.comment.dto.response.RootCommentResponse;
import com.project.numble.application.comment.dto.response.RootsCommentsResponse;
import com.project.numble.application.comment.repository.CommentRepository;
import com.project.numble.application.comment.service.exception.CommentNotAuthException;
import com.project.numble.application.comment.service.exception.CommentNotFoundException;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.repository.UserRepository;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import com.project.numble.core.resolver.UserInfo;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StandardCommentService implements CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public void addComment(UserInfo userInfo, AddCommentRequest request) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow(UserNotFoundException::new);
        Board board = boardRepository.findById(request.getBoardId()).orElseThrow(BoardNotExistsException::new);

        Comment comment = new Comment(request.getContent(), board, user);
        comment.updateAsRoot();

        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void replyContent(UserInfo userInfo, ReplyCommentRequest request) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow(UserNotFoundException::new);
        Board board = boardRepository.findById(request.getBoardId()).orElseThrow(BoardNotExistsException::new);

        Comment parentComment = commentRepository.findByIdWithRootComment(request.getCommentId()).orElseThrow(CommentNotExistsException::new);
        Comment comment = new Comment(request.getContent(), board, user);
        parentComment.updateChildComment(comment);
        commentRepository.save(comment);
        commentRepository.adjustHierarchyOrders(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public GetCommentsResponse getComments(UserInfo userInfo, Long postId,
        PageRequest pageRequest) {
        Board board = boardRepository.findById(postId).orElseThrow(BoardNotExistsException::new);

        List<Comment> comments = commentRepository.findCommentsOrderByHierarchy(pageRequest, board);

        List<GetCommentResponse> getCommentResponses = comments.stream()
            .map(GetCommentResponse::fromComment)
            .collect(Collectors.toList());

        return new GetCommentsResponse(getCommentResponses);
    }

    @Override
    @Transactional(readOnly = true)
    public RootsCommentsResponse getRootComments(UserInfo userInfo, Long postId,
        PageRequest pageRequest) {
        Board board = boardRepository.findById(postId).orElseThrow(BoardNotExistsException::new);

        List<RootCommentResponse> comments = commentRepository.findCommentsWithChildrenCount(pageRequest, board);
        Long count = commentRepository.countCommentsByBoard(board);

        return new RootsCommentsResponse(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public ChildrenCommentsResponse getChildrenComments(UserInfo userInfo,
        ChildrenCommentsRequest request) {
        Board board = boardRepository.findById(request.getBoardId()).orElseThrow(BoardNotExistsException::new);

        List<ChildCommentResponse> comments = commentRepository.findChildrenCommentsByRootCommentId(board, request.getRootCommentId(), request.getLastChildCommentId());

        return ChildrenCommentsResponse.fromComments(comments);
    }

    @Override
    @Transactional
    public void updateComment(UserInfo userInfo, ModCommentRequest request) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(RuntimeException::new); // CommentNotFound같은거여야함

        if (!user.getId().equals(comment.getUser().getId())) {
            throw new CommentNotAuthException();
        }

        comment.updateContent(request.getContent());
    }

    @Override
    @Transactional
    public void deleteComment(UserInfo userInfo, Long commentId) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        if (!user.getId().equals(comment.getUser().getId())) {
            throw new CommentNotAuthException();
        }

        comment.delete();
    }

    @Override
    public List<RootCommentResponse> getAllComments(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistsException::new);

        List<RootCommentResponse> rootComments = commentRepository.findRootComments(board);

        IntStream.range(0, rootComments.size())
            .forEach(i -> {
                if (rootComments.get(i).getChildrenCount() > 0) {
                    List<ChildCommentResponse> childCommentResponses = commentRepository.findChildrenCommentsByRootId(
                        board, rootComments.get(i).getCommentId());
                    rootComments.get(i).setChildren(childCommentResponses);
                }
            });

        return rootComments;
    }

    @Override
    public List<GetCommentResponse> getMyComments(UserInfo userInfo) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow(UserNotFoundException::new);
        List<Comment> comments = commentRepository.findAllByUserOrderByCreatedDateDesc(user);

        return comments.stream()
            .map(GetCommentResponse::fromComment)
            .collect(Collectors.toList());
    }
}
