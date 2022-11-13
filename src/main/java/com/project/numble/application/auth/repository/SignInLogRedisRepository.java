package com.project.numble.application.auth.repository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SignInLogRedisRepository implements SignInLogRepository {

    private static final String KEY_PREFIX = "signIn:log:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(Long userId, Clock clock) {
        String key = getKey(userId);
        redisTemplate.opsForSet().add(key, LocalDateTime.now(clock).format(DateTimeFormatter.ofPattern("yyyy:MM:dd-hh:mm:ss")));
    }

    @Override
    public long countAllByUserId(Long userId) {
        String key = getKey(userId);
        Set<Object> signInLogs = redisTemplate.opsForSet().members(key);

        return signInLogs.size();
    }

    private String getKey(Long userId) {
        return KEY_PREFIX + String.valueOf(userId);
    }
}
