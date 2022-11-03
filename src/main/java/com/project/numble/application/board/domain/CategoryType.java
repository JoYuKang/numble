package com.project.numble.application.board.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_category_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_type_id")
    private Long id;

    @Column(name = "category_type_name", nullable = false)
    private String name;


}
