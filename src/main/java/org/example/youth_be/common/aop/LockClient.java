package org.example.youth_be.common.aop;

public interface LockClient {
    Boolean tryLock(String key, Long waitTime, Long leaseTime) throws InterruptedException;
    void unlock(String key);
}
