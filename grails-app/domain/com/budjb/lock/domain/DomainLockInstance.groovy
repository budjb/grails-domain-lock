package com.budjb.lock.domain

/**
 * Domain responsible for maintaining state for {@link DomainLock} instances.
 */
class DomainLockInstance {
    /**
     * Lock ID.
     */
    String id

    /**
     * Key of the {@link DomainLock} instance that acquired a lock. Will be <code>null</code> when the lock is not acquired.
     */
    String key

    /**
     * Whether the lock has been acquired.
     */
    boolean locked = false

    /**
     * The last time the domain instance was updated.
     */
    Date lastUpdated

    /**
     * Constraints.
     */
    static constraints = {
        id nullable: false, blank: false
        key nullable: true, blank: false
        lastUpdated nullable: true
    }
}
