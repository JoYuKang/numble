package com.project.numble.application.board.repository;

import com.project.numble.application.board.domain.BoardAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardAnimalRepository extends JpaRepository<BoardAnimal, Long> {

}
