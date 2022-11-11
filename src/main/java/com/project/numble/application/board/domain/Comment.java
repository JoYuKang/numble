package com.project.numble.application.board.domain;

import com.project.numble.application.common.entity.BaseTimeEntity;
import com.project.numble.application.user.domain.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tb_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content", nullable = false, length = 120)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id",updatable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",updatable = false)
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReComment> reComments = new ArrayList<>();

    @Builder
    public Comment(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
    }

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
    public void update(String content) {
        this.content = content;
    }

    public void initBoard(Board board) {
        if (this.board == null) {
            this.board = board;
        }
    }

    public void initUser(User user) {
        if (this.user == null) {
            this.user = user;
        }
    }

    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getComments().add(this);
    }
    public void setBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }

}
