package com.project.numble.application.auth.repository;

import java.time.Clock;

public interface SignInLogRepository {

    void save(Long userId, Clock clock);

    long countAllByUserId(Long userId);
}
