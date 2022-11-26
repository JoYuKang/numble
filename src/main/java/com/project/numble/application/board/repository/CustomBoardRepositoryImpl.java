package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.*;
import com.project.numble.application.bookmark.domain.QBookmark;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomBoardRepositoryImpl implements CustomBoardRepository{

    private static final QBoard QBOARD = QBoard.board;

    private static final QBookmark QBOOKMARK = QBookmark.bookmark;

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Board> findBoardsOrderByIdDesc(Pageable pageable, String address, String categoryType) {
        return queryFactory.selectFrom(QBOARD)
                .where(categoryTypeEq(categoryType),
                        addressEq(address))
                .orderBy(QBOARD.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression categoryTypeEq(String categoryType) {
        return categoryType != "전체" ? QBOARD.categoryType.eq(categoryType) : null;
    }

    private BooleanExpression addressEq(String address) {
        return address != "전체" ? QBOARD.boardAddress.eq(address) : null;
    }

    @Override
    public List<Board> findUserBoardsOrderByIdDesc(Pageable pageable, Long userId) {
        return queryFactory.selectFrom(QBOARD)
                .where(QBOARD.user.id.eq(userId))
                .orderBy(QBOARD.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Board> findBookmarkBoardOrderByIdDesc(Pageable pageable, Long userId) {
        return queryFactory.selectFrom(QBOARD)
                .join(QBOARD.bookmarks, QBOOKMARK)
                .on(QBOARD.id.eq(QBOOKMARK.board.id))
                .where(QBOARD.user.id.eq(userId))
                .orderBy(QBOARD.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
