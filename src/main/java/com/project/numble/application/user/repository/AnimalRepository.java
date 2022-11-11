package com.project.numble.application.user.repository;

import com.project.numble.application.user.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    boolean existsByUserId(Long userId);
}
