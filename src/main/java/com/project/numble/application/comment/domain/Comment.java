package com.project.numble.application.comment.domain;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.common.entity.BaseTimeEntity;
import com.project.numble.application.user.domain.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    private static final long DEFAULT_LEFT_FOR_ROOT = 1;
    private static final long DEFAULT_RIGHT_FOR_ROOT = 2;
    private static final long DEFAULT_DEPTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_comment_id")
    private Comment rootComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column(nullable = false)
    private Long leftNode;

    @Column(nullable = false)
    private Long rightNode;

    @Column(nullable = false)
    private Long depth;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private boolean deleted = false;

    public void updateChildComment(Comment childComment) {
        childComment.rootComment = this.rootComment;
        childComment.parentComment = this;
        childComment.depth = this.depth + 1L;
        childComment.leftNode = this.rightNode;
        childComment.rightNode = this.rightNode + 1;
    }

    public void updateAsRoot() {
        this.rootComment = this;
        this.leftNode = DEFAULT_LEFT_FOR_ROOT;
        this.rightNode = DEFAULT_RIGHT_FOR_ROOT;
        this.depth = DEFAULT_DEPTH;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.deleted = true;
    }

    public void deleteBoard() {
        this.board = null;
        this.deleted = true;
    }

    public Comment(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
    }

    public String getAuthorName() {
        return user.getNickname();
    }

    public void initBoard(Board board) {
        this.board = board;
    }
}
