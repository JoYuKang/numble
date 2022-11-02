package com.project.numble.application.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_animal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_type_id")
    private AnimalType animalType;

    @Column(name = "animal_name", nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private boolean neutering;

    public Animal(User user, AnimalType animalType, String name, boolean neutering) {
        this.user = user;
        this.animalType = animalType;
        this.name = name;
        this.neutering = neutering;
    }

    public void initUser(User user) {
        if (this.user == null) {
            this.user = user;
        }
    }
}
