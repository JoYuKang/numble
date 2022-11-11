package com.project.numble.application.helper.factory.entity;

import static org.springframework.test.util.ReflectionTestUtils.*;

import com.project.numble.application.user.domain.Address;
import com.project.numble.application.user.domain.Animal;
import com.project.numble.application.user.domain.User;
import com.project.numble.application.user.domain.enums.AnimalType;
import java.lang.reflect.Constructor;
import java.util.Set;

public final class UserFactory {

    private UserFactory() {
    }

    public static User createStaticUser() {
        User user = User.createNormalUser("test@test.com", "password", "nickname");

        try {
            Constructor<Animal> animalConstructor = Animal.class.getDeclaredConstructor();
            animalConstructor.setAccessible(true);

            Animal dog = animalConstructor.newInstance();
            setField(dog, "animalType", AnimalType.DOG);

            Animal cat = animalConstructor.newInstance();
            setField(cat, "animalType", AnimalType.CAT);

            setField(user, "animals", Set.of(dog, cat));

            Address address = new Address();

            setField(address, "addressName", "서울시 강서구");
            setField(address, "regionDepth1", "서울시");
            setField(address, "regionDepth2", "강서구");

            setField(user, "address", address);

            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
