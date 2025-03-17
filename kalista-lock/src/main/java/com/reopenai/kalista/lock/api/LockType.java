package com.reopenai.kalista.lock.api;

/**
 * 锁类型
 *
 * @author Allen Huang
 */
public enum LockType {
    /**
     * 可重入锁
     */
    REENTRANT,
    /**
     * 公平锁
     */
    FAIR,
    /**
     * 读写锁中的读锁
     */
    READ_LOCK,
    /**
     * 读写锁中的写锁
     */
    WRITE_LOCK,
}
