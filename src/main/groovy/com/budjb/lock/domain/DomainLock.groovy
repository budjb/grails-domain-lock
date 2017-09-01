package com.budjb.lock.domain

import groovy.transform.CompileStatic

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock

/**
 * A {@link Lock} implementation backed by a Grails domain. This class is tightly
 * coupled with {@link DomainLockInstance}.
 */
@CompileStatic
class DomainLock implements Lock {
    /**
     * Default polling interval for re-checking whether a lock can be acquired.
     */
    static final long DEFAULT_POLL_INTERVAL = 1000L

    /**
     * Polling interval (in milliseconds) for re-checking whether a lock can be acquired.
     */
    long pollInterval = DEFAULT_POLL_INTERVAL

    /**
     * Lock ID.
     */
    final String id

    /**
     * Lock instance key.
     */
    final String key

    /**
     * Constructor.
     *
     * @param id ID of the lock.
     */
    DomainLock(String id) {
        this.id = id
        this.key = UUID.randomUUID().toString()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void lock() {
        while (!attemptLock()) {
            sleep(pollInterval)
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean tryLock() {
        return attemptLock()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long end = System.currentTimeMillis() + unit.toMillis(time)

        while (System.currentTimeMillis() < end) {
            if (attemptLock()) {
                return true
            }
            else {
                sleep(pollInterval)
            }
        }

        return false
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void unlock() {
        DomainLockInstance.withNewSession {
            DomainLockInstance.withNewTransaction {
                DomainLockInstance instance = DomainLockInstance.findById(id)

                if (!instance) {
                    return null
                }

                if (!instance.isLocked()) {
                    return null
                }

                if (instance.getKey() != key) {
                    return null
                }

                instance.setLocked(false)
                instance.setKey(null)
                instance.save(flush: true, failOnError: true)
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Condition newCondition() {
        throw new UnsupportedOperationException()
    }

    /**
     * Attempts to acquire the lock with this instance's key.
     *
     * @return Whether the lock was acquired.
     */
    protected boolean attemptLock() {
        return DomainLockInstance.withNewSession {
            return DomainLockInstance.withNewTransaction {
                DomainLockInstance instance = DomainLockInstance.findById(id)

                if (!instance) {
                    instance = new DomainLockInstance()
                    instance.id = id
                }
                else {
                    instance.refresh()
                }

                if (instance.isLocked()) {
                    return false
                }

                instance.setLocked(true)
                instance.setId(id)
                instance.setKey(key)
                instance.save(flush: true, failOnError: true)

                return true
            }
        }
    }
}
