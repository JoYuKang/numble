package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.*;
import com.project.numble.application.bookmark.domain.QBookmark;
import com.project.numble.application.user.domain.enums.AnimalType;
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

    private static final QBoardAnimal QBOARDANIMAL = QBoardAnimal.boardAnimal;

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Board> findBoardsOrderByIdDesc(String address, String animalType, String categoryType, Long lastBoardId) {
        return queryFactory.selectFrom(QBOARD).distinct()
                .join(QBOARD.boardAnimals, QBOARDANIMAL)
                .on(QBOARD.id.eq(QBOARDANIMAL.board.id))
                .where(categoryTypeEq(categoryType),
                        addressEq(address),
                        boardAnimalEq(animalType),
                        QBOARD.id.lt(lastBoardId))
                .orderBy(QBOARD.id.desc())
                .limit(5)
                .fetch();
    }

    private BooleanExpression categoryTypeEq(String categoryType) {
        return categoryType != null && !categoryType.equals("전체") ? QBOARD.categoryType.eq(categoryType) : null;
    }

    private BooleanExpression addressEq(String address) {
        return address != null && !address.equals("전체") ? QBOARD.boardAddress.eq(address) : null;
    }

    private static BooleanExpression boardAnimalEq(String animalType) {
        return animalType != null && !animalType.equals("전체") ? QBOARDANIMAL.animalType.eq(AnimalType.getAnimalType(animalType)) : null;
    }

    @Override
    public List<Board> findUserBoardsOrderByIdDesc(Long userId, Long lastBoardId) {
        return queryFactory.selectFrom(QBOARD)
                .where(QBOARD.user.id.eq(userId),
                        QBOARD.id.lt(lastBoardId))
                .orderBy(QBOARD.id.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<Board> findBookmarkBoardOrderByIdDesc(Long userId, Long lastBoardId) {
        return queryFactory.selectFrom(QBOARD)
                .join(QBOARD.bookmarks, QBOOKMARK)
                .on(QBOARD.id.eq(QBOOKMARK.board.id),
                        QBOARD.id.lt(lastBoardId))
                .where(QBOARD.user.id.eq(userId))
                .orderBy(QBOARD.id.desc())
                .limit(5)
                .fetch();
    }
}
