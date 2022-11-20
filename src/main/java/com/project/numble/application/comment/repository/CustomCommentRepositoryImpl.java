package com.project.numble.application.comment.repository;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.comment.domain.Comment;
import com.project.numble.application.comment.domain.QComment;
import com.project.numble.application.comment.dto.response.ChildCommentResponse;
import com.project.numble.application.comment.dto.response.RootCommentResponse;
import com.project.numble.application.user.domain.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private static final QComment QCOMMENT = QComment.comment;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentsOrderByHierarchy(Pageable pageable, Board board) {
        return selectCommentInnerFetchJoinAccount()
            .where(isActiveCommentOf(board))
            .orderBy(QCOMMENT.rootComment.id.asc(), QCOMMENT.leftNode.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public Optional<Comment> findByIdWithAuthor(Long id) {
        Comment comment = selectCommentInnerFetchJoinAccount()
            .where(isActiveComment(id))
            .fetchOne();

        return Optional.ofNullable(comment);
    }

    @Override
    public Optional<Comment> findByIdWithRootComment(Long id) {
        Comment comment = queryFactory.selectFrom(QCOMMENT)
            .innerJoin(QCOMMENT.rootComment)
            .fetchJoin()
            .where(isActiveComment(id))
            .fetchOne();

        return Optional.ofNullable(comment);
    }

    @Override
    public Optional<Comment> findByIdWithRootCommentAndAuthor(Long id) {
        Comment comment = selectCommentInnerFetchJoinAccount()
            .innerJoin(QCOMMENT.rootComment)
            .fetchJoin()
            .where(isActiveComment(id))
            .fetchOne();

        return Optional.ofNullable(comment);
    }

    @Override
    public void adjustHierarchyOrders(Comment newComment) {
        queryFactory.update(QCOMMENT)
            .set(QCOMMENT.leftNode, QCOMMENT.leftNode.add(2))
            .where(QCOMMENT.leftNode.goe(newComment.getRightNode())
                .and(isActiveCommentInSameGroupExceptNewComment(newComment)))
            .execute();

        queryFactory.update(QCOMMENT)
            .set(QCOMMENT.rightNode, QCOMMENT.rightNode.add(2))
            .where(QCOMMENT.rightNode.goe(newComment.getLeftNode())
                .and(isActiveCommentInSameGroupExceptNewComment(newComment)))
            .execute();
    }

    @Override
    public void deleteChildComments(Comment parentComment) {
        deleteComment()
            .where(QCOMMENT.leftNode.gt(parentComment.getLeftNode())
                .and(QCOMMENT.rightNode.lt(parentComment.getRightNode())
                    .and(QCOMMENT.rootComment.eq(parentComment.getRootComment()))
                    .and(isActiveComment())))
            .execute();
    }

    @Override
    public Long countCommentsByBoard(Board board) {
        return queryFactory.select(QCOMMENT.count())
            .from(QCOMMENT)
            .where(isActiveCommentOf(board))
            .fetchOne();
    }

    @Override
    public void deleteAllByBoard(Board board) {
        deleteComment()
            .where(isActiveCommentOf(board))
            .execute();
    }

    @Override
    public List<RootCommentResponse> findCommentsWithChildrenCount(Pageable pageable, Board board) {
        QComment parent = new QComment("parent");
        QComment child = new QComment("child");

        return queryFactory
            .select(
                Projections.fields(RootCommentResponse.class,
                    parent.id.as("commentId"),
                    parent.user.nickname.as("author"),
                    parent.content.as("content"),
                    parent.depth.as("depth"),
                    parent.createdDate.as("createdDate"),
                    ExpressionUtils.as(
                        JPAExpressions
                            .select(child.count())
                            .from(child)
                            .where(child.leftNode.gt(parent.leftNode), child.rightNode.lt(parent.rightNode)),
                        "childrenCount"
                    )
                )
            )
            .from(parent)
            .innerJoin(parent.user)
            .where(parent.board.eq(board).and(parent.deleted.eq(false)), parent.depth.lt(2))
            .orderBy(parent.rootComment.id.asc(), parent.leftNode.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public List<ChildCommentResponse> findChildrenCommentsByRootCommentId(Board board,
        Long rootCommentId, Long commentId) {
        return queryFactory
            .select(
                Projections.fields(ChildCommentResponse.class,
                    QCOMMENT.id.as("commentId"),
                    QCOMMENT.user.nickname.as("author"),
                    QCOMMENT.content.as("content"),
                    QCOMMENT.depth.as("depth"),
                    QCOMMENT.createdDate.as("createdDate"),
                    QCOMMENT.rootComment.id.as("rootId"),
                    QCOMMENT.parentComment.id.as("parentId"),
                    QCOMMENT.parentComment.user.nickname.as("parentAuthor")
                )
            )
            .from(QCOMMENT)
            .innerJoin(QCOMMENT.user)
            .innerJoin(QCOMMENT.parentComment.user)
            .where(QCOMMENT.board.eq(board).and(QCOMMENT.parentComment.deleted.eq(false)))
            .where(QCOMMENT.depth.gt(1))
            .where(QCOMMENT.rootComment.id.eq(rootCommentId))
            .orderBy(QCOMMENT.leftNode.asc())
            .fetch();
    }

    @Override
    public List<RootCommentResponse> findRootComments(Board board) {
        QComment parent = new QComment("parent");
        QComment child = new QComment("child");

        return queryFactory
            .select(
                Projections.fields(RootCommentResponse.class,
                    parent.id.as("commentId"),
                    parent.user.nickname.as("author"),
                    parent.content.as("content"),
                    parent.depth.as("depth"),
                    parent.createdDate.as("createdDate"),
                    parent.user.address.regionDepth2.as("address"),
                    parent.deleted.as("deleted"),
                    ExpressionUtils.as(
                        JPAExpressions
                            .select(child.count())
                            .from(child)
                            .where(child.leftNode.gt(parent.leftNode), child.rightNode.lt(parent.rightNode)),
                        "childrenCount"
                    )
                )
            )
            .from(parent)
            .innerJoin(parent.user)
            .where(parent.board.eq(board), parent.depth.lt(2))
            .orderBy(parent.rootComment.id.asc(), parent.leftNode.asc())
            .fetch();
    }

    @Override
    public List<ChildCommentResponse> findChildrenCommentsByRootId(Board board, Long rootCommentId) {
        return queryFactory
            .select(
                Projections.fields(ChildCommentResponse.class,
                    QCOMMENT.id.as("commentId"),
                    QCOMMENT.user.nickname.as("author"),
                    QCOMMENT.content.as("content"),
                    QCOMMENT.depth.as("depth"),
                    QCOMMENT.createdDate.as("createdDate"),
                    QCOMMENT.rootComment.id.as("rootId"),
                    QCOMMENT.parentComment.id.as("parentId"),
                    QCOMMENT.parentComment.user.nickname.as("parentAuthor"),
                    QCOMMENT.user.address.regionDepth2.as("address"),
                    QCOMMENT.deleted.as("deleted")
                )
            )
            .from(QCOMMENT)
            .innerJoin(QCOMMENT.user)
            .innerJoin(QCOMMENT.parentComment.user)
            .where(QCOMMENT.board.eq(board))
            .where(QCOMMENT.depth.gt(1))
            .where(QCOMMENT.rootComment.id.eq(rootCommentId))
            .orderBy(QCOMMENT.leftNode.asc())
            .fetch();
    }


    private JPAQuery<Comment> selectCommentInnerFetchJoinAccount() {
        return queryFactory.selectFrom(QCOMMENT)
            .innerJoin(QCOMMENT.user)
            .fetchJoin();
    }

    private BooleanExpression isActiveCommentOf(User user) {
        return QCOMMENT.user.eq(user).and(isActiveComment());
    }

    private BooleanExpression isActiveCommentOf(Board board) {
        return QCOMMENT.board.eq(board).and(isActiveComment());
    }

    private BooleanExpression isActiveComment(Long id) {
        return QCOMMENT.id.eq(id).and(isActiveComment());
    }

    private BooleanExpression isActiveComment() {
        return QCOMMENT.deleted.eq(false);
    }

    private Predicate isActiveCommentInSameGroupExceptNewComment(Comment newComment) {
        return QCOMMENT.rootComment.eq(newComment.getRootComment())
            .and(QCOMMENT.ne(newComment))
            .and(isActiveComment());
    }

    private JPAUpdateClause deleteComment() {
        return queryFactory.update(QCOMMENT)
            .set(QCOMMENT.deleted, true);
    }
}
