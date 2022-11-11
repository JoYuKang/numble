package com.project.numble.application.user.domain.enums;

import java.util.Arrays;

public enum AnimalType {
    DOG("강아지"),
    CAT("고양이"),
    FISH("물고기"),
    HAMSTER("햄스터"),
    REPTILE("파충류"),
    BIRD("새"),
    RABBIT("토끼"),
    ETC("기타");

    AnimalType(String name) {
        this.name = name;
    }

    private final String name;

    public static String getName(AnimalType type) {
        return Arrays
                .stream(AnimalType.values())
                .filter(animal -> animal == type)
                .map(animal -> animal.name)
                .findAny()
                .orElseThrow();
    }

    public static AnimalType getAnimalType(String name) {
        return Arrays
                .stream(AnimalType.values())
                .filter(animal -> animal.name.equals(name))
                .findAny()
                .orElseThrow();
    }
}
