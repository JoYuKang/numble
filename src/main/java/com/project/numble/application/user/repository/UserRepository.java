package com.project.numble.application.user.repository;

import com.project.numble.application.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @EntityGraph(attributePaths = {"address", "animals"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findStaticUserInfoById(Long userId);
}
