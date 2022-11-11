package com.project.numble.application.user.repository;

import com.project.numble.application.user.domain.Address;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
