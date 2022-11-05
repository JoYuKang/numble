package com.project.numble.application.user.domain;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.board.domain.Comment;
import com.project.numble.application.common.entity.BaseTimeEntity;
import com.project.numble.application.user.domain.enums.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> address = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animals = new ArrayList<>();

    // board 추가
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    // 댓글 추가
    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Comment> Comments = new ArrayList<>();


    private boolean deleted = false;

    private LocalDateTime deletedDate;

    private User(String email, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void addAddress(Address address) {
        this.address.add(address);
        address.initUser(this);
    }

    public void addAnimal(Animal animal) {
        this.animals.add(animal);
        animal.initUser(this);
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

}
