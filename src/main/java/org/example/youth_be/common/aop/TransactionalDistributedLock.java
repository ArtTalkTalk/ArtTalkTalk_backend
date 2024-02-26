package org.example.youth_be.common.aop;

import org.example.youth_be.common.enums.LockUsageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransactionalDistributedLock {
    /**
     * 락의 이름
     */
    String key();

    /**
     * 락을 기다리는 시간
     * 동시에 들어오는 요청은 기다릴 필요가 없기 때문에 0초
     */
    long waitTime() default 0L;

    /**
     * 락 임대 시간
     * 3분이 지나면 락을 해제한다
     */
    long leaseTime() default 180L;

    /**
     * 락 종류
     */
    LockUsageType usage = null;
}
