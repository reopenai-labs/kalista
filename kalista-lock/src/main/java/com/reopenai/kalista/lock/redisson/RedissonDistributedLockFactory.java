package com.reopenai.kalista.lock.redisson;

import com.reopenai.kalista.base.ErrorCode;
import com.reopenai.kalista.core.lang.exception.BusinessException;
import com.reopenai.kalista.lock.api.DistributedLockFactory;
import com.reopenai.kalista.lock.api.DistributedLocker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by Allen Huang
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonDistributedLockFactory implements DistributedLockFactory {

    private final RedissonClient redissonClient;

    @Override
    public DistributedLocker newReentrantLock(String key) {
        return new RedissonDistributedLocker(redissonClient.getLock(key));
    }

    @Override
    public DistributedLocker lockReentrantLock(String key) {
        return lockReentrantLock(key, 60000);
    }

    @Override
    public DistributedLocker lockReentrantLock(String key, long timeout) {
        return lockReentrantLock(key, timeout, -1);
    }

    @Override
    public DistributedLocker lockReentrantLock(String key, long timeout, long leaseTime) {
        DistributedLocker lockInstance = newReentrantLock(key);
        return tryLock(lockInstance, key, timeout, leaseTime);
    }

    @Override
    public DistributedLocker newFairLock(String key) {
        return new RedissonDistributedLocker(redissonClient.getFairLock(key));
    }

    @Override
    public DistributedLocker lockFairLock(String key) {
        return lockFairLock(key, 60000);
    }

    @Override
    public DistributedLocker lockFairLock(String key, long timeout) {
        return lockFairLock(key, timeout, -1);
    }

    @Override
    public DistributedLocker lockFairLock(String key, long timeout, long leaseTime) {
        DistributedLocker lockInstance = newFairLock(key);
        return tryLock(lockInstance, key, timeout, leaseTime);
    }

    @Override
    public DistributedLocker newReadLock(String key) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
        RLock rLock = readWriteLock.readLock();
        return new RedissonDistributedLocker(rLock);
    }

    @Override
    public DistributedLocker lockReadLock(String key) {
        return lockReadLock(key, 60000);
    }

    @Override
    public DistributedLocker lockReadLock(String key, long timeout) {
        return lockReadLock(key, timeout, -1);
    }

    @Override
    public DistributedLocker lockReadLock(String key, long timeout, long leaseTime) {
        DistributedLocker lockInstance = newReadLock(key);
        return tryLock(lockInstance, key, timeout, leaseTime);
    }

    @Override
    public DistributedLocker newWriteLock(String key) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(key);
        RLock rLock = readWriteLock.writeLock();
        return new RedissonDistributedLocker(rLock);
    }

    @Override
    public DistributedLocker lockWriteLock(String key) {
        return lockWriteLock(key, 60000);
    }

    @Override
    public DistributedLocker lockWriteLock(String key, long timeout) {
        return lockWriteLock(key, timeout, -1);
    }

    @Override
    public DistributedLocker lockWriteLock(String key, long timeout, long leaseTime) {
        DistributedLocker lockInstance = newWriteLock(key);
        return tryLock(lockInstance, key, timeout, leaseTime);
    }

    private DistributedLocker tryLock(DistributedLocker lockInstance, String key, long timeout, long leaseTime) {
        try {
            if (lockInstance.tryLock(timeout, leaseTime, TimeUnit.MILLISECONDS)) {
                return lockInstance;
            }
        } catch (InterruptedException e) {
            log.error("[Redisson]获取分布式锁失败", e);
            lockInstance.unlock();
        }
        log.error("[Redisson]未获取到锁.lock name:{}, timeout:{}ms, leaseTime:{}ms", key, timeout, leaseTime);
        throw new BusinessException(ErrorCode.Builtin.DISTRIBUTED_LOCK_ACQUIRE_LOCK_TIMEOUT);
    }

}
