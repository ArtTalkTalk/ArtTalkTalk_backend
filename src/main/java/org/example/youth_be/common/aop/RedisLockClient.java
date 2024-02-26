package org.example.youth_be.common.aop;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisLockClient implements LockClient {
    private final RedissonClient redissonClient;

    @Override
    public Boolean tryLock(String key, Long waitTime, Long leaseTime) throws InterruptedException {
        return redissonClient.getLock(key).tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
    }

    @Override
    public void unlock(String key) {
        redissonClient.getLock(key).unlock();
    }
}
