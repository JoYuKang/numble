package com.project.numble.application.board.domain;

import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.domain.enums.AnimalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_board_animal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "animal_type")
    private AnimalType animalType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public BoardAnimal(AnimalType animalType) {
        this.animalType = animalType;
    }

    public void initUser(Board board) {
        if (this.board == null) {
            this.board = board;
        }
    }
}
