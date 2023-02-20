package com.c103.dolbom.user.repository;

import com.c103.dolbom.user.common.JwtProperties;
import com.c103.dolbom.user.dto.RefreshToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private RedisTemplate redisTemplate;

    public RefreshTokenRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(final RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(
                refreshToken.getEmail(),
                refreshToken.getRefreshToken(),
                JwtProperties.REFRESH_EXP_TIME,
                TimeUnit.MILLISECONDS
        );
    }

    public Optional<RefreshToken> findById(final String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String email = valueOperations.get(refreshToken);

        if (Objects.isNull(email)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, email));
    }
}
