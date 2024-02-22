package org.example.youth_be.common.redis;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenRedisRepository implements TokenRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean hasBlackList(@NotNull String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) != null) { // 블랙리스트로 되어 있는 경우 true
            return true;
        }
        return false;
    }

    @Override
    public void setBlackList(@NotNull String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }
}