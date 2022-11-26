package com.project.numble.application.like.domain;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.user.domain.User;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    public Like(User user, Board board) {
        this.user = user;
        this.board = board;
    }
    public void initBoard(Board board) {
        if (this.board == null) {
            this.board = board;
        }
    }
}
