package com.project.numble.application.board.domain;

import com.project.numble.application.board.dto.request.ModBoardRequest;
import com.project.numble.application.comment.domain.Comment;
import com.project.numble.application.common.entity.BaseTimeEntity;
import com.project.numble.application.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Table(name = "tb_board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",updatable = false) // 읽기 전용 insertable = false
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardAnimal> boardAnimals = new ArrayList<>();

    @Column(nullable = false)
    private String boardAddress;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String categoryType;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "view_count")
    private int viewCount = 0;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bookmark> bookmarks = new HashSet<>();

    @Column(name = "bookmark_count")
    private Integer bookmarkCount = 0;

    @Builder
    private Board(User user, String content, String boardAddress, List<Image> images, String categoryType, List<BoardAnimal> boardAnimals) {
        this.user = user;
        this.content = content;
        this.boardAddress = boardAddress;
        this.images = images;
        this.categoryType = categoryType;
        this.boardAnimals = boardAnimals;
    }


    // 이미지 등록
    public void addImage(Image image) {
        this.images.add(image);
    }

    // 수정
    public void update(ModBoardRequest request, String boardAddress) {
        this.content = request.getContent();
        this.categoryType = request.getCategoryType();
        this.boardAddress = boardAddress;
    }

    // 조회수 + 1
    public void plusViewCount() {
        this.viewCount = this.getViewCount() + 1;
    }

    public void plusLikeCount() {
        this.likeCount = this.getLikeCount() + 1;
    }
    public void minusLikeCount() {
        this.likeCount = this.getLikeCount() - 1;
    }

    public void addLike(Like like){
        this.likes.add(like);
        like.initBoard(this);
    }

    public void delLike(Like like){
        this.likes.remove(like);
        like.initBoard(this);
    }
    public void plusBookmarkCount() {
        this.bookmarkCount = this.getBookmarkCount() + 1;
    }
    public void minusBookmarkCount() {
        this.bookmarkCount = this.getBookmarkCount() - 1;
    }

    public void addBookmark(Bookmark bookmark){
        this.bookmarks.add(bookmark);
        bookmark.initBoard(this);
    }

    public void delBookmark(Bookmark bookmark){
        this.bookmarks.remove(bookmark);
        bookmark.initBoard(this);
    }

    // 동물 추가
    public void addBoardAnimal(BoardAnimal boardAnimal) {
        this.boardAnimals.add(boardAnimal);
        boardAnimal.initUser(this);
    }

    // 댓글 추가
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.initBoard(this);
    }

    // 댓글 삭제
    public void delComment(Comment comment) {
        this.comments.add(comment);
    }

    public void initUser(User user) {
        if (this.user == null) {
            this.user = user;
        }
    }
}
