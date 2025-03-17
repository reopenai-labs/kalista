package com.reopenai.kalista.lock.api;

/**
 * 分布式锁模版接口，提供了获取分布式锁的能力
 *
 * @author Allen Huang
 */
public interface DistributedLockFactory {

    /**
     * 创建一个分布式锁实例
     *
     * @param key 锁的key
     * @return 返回分布式锁实例
     */
    DistributedLocker newReentrantLock(String key);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 在获取锁时，最大的等待时间为1分钟。如果1分钟之后未能获取到锁，则将抛出异常.
     * 此锁的最长租赁期为5分钟。
     *
     * @param key 锁的key
     * @return 返回分布式锁实例
     */
    DistributedLocker lockReentrantLock(String key);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 如果在指定时间内没有获取到锁，则抛出异常
     * 此锁的最长租赁期为5分钟。
     *
     * @param key     锁的key
     * @param timeout 超时时间(ms)
     * @return 返回锁对象
     */
    DistributedLocker lockReentrantLock(String key, long timeout);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 如果在指定时间内没有获取到锁，则抛出异常
     *
     * @param key       锁的key
     * @param timeout   超时时间(ms)
     * @param leaseTime 锁的租赁时间(ms)
     * @return 返回锁对象
     */
    DistributedLocker lockReentrantLock(String key, long timeout, long leaseTime);

    /**
     * 创建一个分布式锁实例
     *
     * @param key 锁的key
     * @return 返回分布式锁实例
     */
    DistributedLocker newFairLock(String key);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 在获取锁时，最大的等待时间为1分钟。如果1分钟之后未能获取到锁，则将抛出异常.
     * 此锁的最长租赁期为5分钟。
     *
     * @param key 锁的key
     * @return 返回分布式锁实例
     */
    DistributedLocker lockFairLock(String key);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 如果在指定时间内没有获取到锁，则抛出异常
     * 此锁的最长租赁期为5分钟。
     *
     * @param key     锁的key
     * @param timeout 超时时间(ms)
     * @return 返回锁对象
     */
    DistributedLocker lockFairLock(String key, long timeout);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 如果在指定时间内没有获取到锁，则抛出异常
     *
     * @param key       锁的key
     * @param timeout   超时时间(ms)
     * @param leaseTime 锁的租赁时间(ms)
     * @return 返回锁对象
     */
    DistributedLocker lockFairLock(String key, long timeout, long leaseTime);

    /**
     * 创建一个分布式锁实例
     *
     * @param key 锁的key
     * @return 返回分布式锁实例
     */
    DistributedLocker newReadLock(String key);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 在获取锁时，最大的等待时间为1分钟。如果1分钟之后未能获取到锁，则将抛出异常.
     * 此锁的最长租赁期为5分钟。
     *
     * @param key 锁的key
     * @return 返回分布式锁实例
     */
    DistributedLocker lockReadLock(String key);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 如果在指定时间内没有获取到锁，则抛出异常
     * 此锁的最长租赁期为5分钟。
     *
     * @param key     锁的key
     * @param timeout 超时时间(ms)
     * @return 返回锁对象
     */
    DistributedLocker lockReadLock(String key, long timeout);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 如果在指定时间内没有获取到锁，则抛出异常
     *
     * @param key       锁的key
     * @param timeout   超时时间(ms)
     * @param leaseTime 锁的租赁时间(ms)
     * @return 返回锁对象
     */
    DistributedLocker lockReadLock(String key, long timeout, long leaseTime);

    /**
     * 创建一个分布式锁实例
     *
     * @param key 锁的key
     * @return 返回分布式锁实例
     */
    DistributedLocker newWriteLock(String key);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 在获取锁时，最大的等待时间为1分钟。如果1分钟之后未能获取到锁，则将抛出异常.
     * 此锁的最长租赁期为5分钟。
     *
     * @param key 锁的key
     * @return 返回分布式锁实例
     */
    DistributedLocker lockWriteLock(String key);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 如果在指定时间内没有获取到锁，则抛出异常
     * 此锁的最长租赁期为5分钟。
     *
     * @param key     锁的key
     * @param timeout 超时时间(ms)
     * @return 返回锁对象
     */
    DistributedLocker lockWriteLock(String key, long timeout);

    /**
     * 创建一个分布式锁实例，创建完成之后尝试加锁。
     * 如果在指定时间内没有获取到锁，则抛出异常
     *
     * @param key       锁的key
     * @param timeout   超时时间(ms)
     * @param leaseTime 锁的租赁时间(ms)
     * @return 返回锁对象
     */
    DistributedLocker lockWriteLock(String key, long timeout, long leaseTime);

}
