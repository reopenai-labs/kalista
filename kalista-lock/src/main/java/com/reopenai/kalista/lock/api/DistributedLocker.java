package com.reopenai.kalista.lock.api;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by Allen Huang
 */
public interface DistributedLocker extends Lock {

    /**
     * 获取锁的名字
     *
     * @return 返回锁名字
     */
    String getName();

    /**
     * 获取一个具有租赁时间的锁。如果锁被占用，则将会一直等待锁可用.
     * 在获得锁之后，将会在定义的<code>leaseTime</code>时间间隔后，自动释放锁.
     *
     * @param leaseTime 锁的租赁时间
     * @param unit      时间单位
     * @throws InterruptedException 如果在等待获取锁的过程中，线程被中断，则抛出此异常
     */
    void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException;

    /**
     * 尝试去获取一个具有祖灵时间的锁，如果锁被占用，则将会等待锁的释放。等待时间不超过<code>waitTime</code>.
     * 在获得锁之后，将会在定义的<code>leaseTime</code>时间间隔后，自动释放锁
     *
     * @param waitTime  最大等待锁的释放时间
     * @param leaseTime 锁的租赁时间
     * @param unit      时间单位
     * @return 如果成功获取锁，则返回true，否则将返回false
     * @throws InterruptedException 如果在等待获取锁的过程中，线程被中断，则抛出此异常.
     */
    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    /**
     * 获取一个具有租赁时间的锁。如果锁被占用，则将会一直等待锁可用.
     * 在获得锁之后，将会在定义的<code>leaseTime</code>时间间隔后，自动释放锁.
     *
     * @param leaseTime 锁的租赁时间
     * @param unit      时间单位
     */
    void lock(long leaseTime, TimeUnit unit);

    /**
     * 与状态无关的解锁
     *
     * @return 如果存在锁并且锁已经被释放，则返回true，否则返回false
     */
    boolean forceUnlock();

    /**
     * 检查锁是否被某个线程锁定.
     *
     * @return 如果已经锁定，则返回true，否则返回false
     */
    boolean isLocked();

    /**
     * 检查锁是否被某个定义了线程id的线程持有
     *
     * @param threadId 线程id
     * @return 如果具有给定id的线程持有了锁，则返回true，否则返回false
     */
    boolean isHeldByThread(long threadId);

    /**
     * 检查锁是否被当前线程持有
     *
     * @return 如果被当前线程持有，则返回true，否则返回false
     */
    boolean isHeldByCurrentThread();

    /**
     * 当前线程持有的锁数量
     *
     * @return 如果当前线程未持有此锁，则返回0
     */
    int getHoldCount();

    /**
     * 锁的剩余时间
     *
     * @return 返回锁的剩余时间的毫秒数.如果不存在，则返回-2，如果该锁存在，但是没有设置租赁时间，则返回-1
     */
    long remainTimeToLive();

}
