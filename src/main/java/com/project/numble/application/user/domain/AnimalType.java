package com.project.numble.application.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_animal_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimalType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_type_id")
    private Long id;

    @Column(name = "animal_type_name", nullable = false)
    private String name;
}
