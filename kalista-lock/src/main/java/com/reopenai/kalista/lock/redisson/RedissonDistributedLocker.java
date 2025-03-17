package com.reopenai.kalista.lock.redisson;

import com.reopenai.kalista.lock.api.DistributedLocker;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.lang.NonNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * Created by Allen Huang
 */
@RequiredArgsConstructor
public class RedissonDistributedLocker implements DistributedLocker {

    private final RLock lock;

    @Override
    public String getName() {
        return lock.getName();
    }

    @Override
    public void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException {
        lock.lockInterruptibly(leaseTime, unit);
    }

    @Override
    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(waitTime, leaseTime, unit);
    }

    @Override
    public void lock(long leaseTime, TimeUnit unit) {
        lock.lock(leaseTime, unit);
    }

    @Override
    public void lock() {
        this.lock(-1, TimeUnit.MINUTES);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
        return lock.tryLock();
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return lock.tryLock(time, unit);
    }

    @Override
    public void unlock() {
        if (lock.getHoldCount() > 0) {
            lock.unlock();
        }
    }

    @Override
    public @NonNull Condition newCondition() {
        return lock.newCondition();
    }

    @Override
    public boolean forceUnlock() {
        return lock.forceUnlock();
    }

    @Override
    public boolean isLocked() {
        return lock.isLocked();
    }

    @Override
    public boolean isHeldByThread(long threadId) {
        return lock.isHeldByThread(threadId);
    }

    @Override
    public boolean isHeldByCurrentThread() {
        return lock.isHeldByCurrentThread();
    }

    @Override
    public int getHoldCount() {
        return lock.getHoldCount();
    }

    @Override
    public long remainTimeToLive() {
        return lock.remainTimeToLive();
    }

}
