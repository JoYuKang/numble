package com.project.numble.application.image.repository;

import com.project.numble.application.board.domain.Board;
import com.project.numble.application.image.domain.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByBoard(Board board);
}
