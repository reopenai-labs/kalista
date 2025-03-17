package com.reopenai.kalista.lock.annotation;

import com.reopenai.kalista.lock.api.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Allen Huang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    /**
     * 声明的锁名称是否包含服务前缀，默认为true
     */
    boolean includePrefix() default true;

    /**
     * 使用SpEL声明的分布式锁的Key.默认为 类名 + 方法名 + 参数
     */
    String value() default "";

    /**
     * 锁的类型，支持公平锁和可重入锁
     */
    LockType type() default LockType.REENTRANT;

    /**
     * 获取锁的超时时间，默认为一分钟
     */
    long timeout() default 60000;

    /**
     * 持有锁的超时时间，默认为永续
     */
    long leaseTime() default -1;

}
