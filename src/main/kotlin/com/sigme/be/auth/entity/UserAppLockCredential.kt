package com.sigme.be.auth.entity

import com.sigme.be.global.entity.UpdatableEntity
import com.sigme.be.user.entity.User
import jakarta.persistence.*
import java.time.Instant

@Entity(name = "app_lock_credential")
class UserAppLockCredential(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    val user: User,

    pinHash: String
) : UpdatableEntity() {
    @get:Transient
    val isLocked: Boolean
        get() = lockedAt != null

    @Column(nullable = false)
    var pinHash: String = pinHash
        protected set

    @Column(nullable = false)
    var failedAttemptCount: Int = 0
    protected set

    @Column
    var lockedAt: Instant? = null
        protected set

    fun recordFailedAttempt() {
        check(!isLocked) {
            "잠금 상태 중엔 비밀번호를 입력할 수 없습니다."
        }
        failedAttemptCount++
    }

    fun resetFailedAttempts() {
        failedAttemptCount = 0
    }

    fun changePinHash(pinHash: String) {
        this.pinHash = pinHash
        resetFailedAttempts()
        unlock()
    }

    fun lock(lockedAt: Instant) {
        this.lockedAt = lockedAt
    }

    fun unlock() {
        this.lockedAt = null
    }
}