package com.project.numble.application.user.domain;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.common.entity.BaseTimeEntity;
import com.project.numble.application.user.domain.enums.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", nullable = false, length = 20)
    private String email;

    private String password;

    @Column(name = "user_nickname", nullable = false, length = 20)
    private String nickname;

    private String profile;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Animal> animals = new HashSet<>();

    // board 추가
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    // 댓글 추가
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private boolean deleted = false;

    private LocalDateTime deletedDate;

    private User(String email, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void addAnimal(Animal animal) {
        this.animals.add(animal);
        animal.initUser(this);
    }

    public void addBoard(Board board) {
        this.boards.add(board);
        board.initUser(this);
    }

    public void delBoard(Board board) {
        this.boards.remove(board);
    }

    public static User createNormalUser(String email, String password, String nickname) {
        User user = new User(email, nickname);

        user.password = password;
        return user;
    }

    public static User createSocialUser(String email, String nickname, String profile) {
        User user = new User(email, nickname);

        user.profile = profile;
        return user;
    }

    public void withdrawal() {
        this.deleted = true;
    }
}
