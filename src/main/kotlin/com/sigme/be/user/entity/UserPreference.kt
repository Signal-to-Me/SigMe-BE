package com.sigme.be.user.entity

import com.sigme.be.global.entity.UpdatableEntity
import jakarta.persistence.*

@Entity
class UserPreference private constructor(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false, unique = true)
    val user: User,

    isDailyPushEnabled: Boolean
) : UpdatableEntity() {
    @Column(name = "is_daily_push_enabled", nullable = false)
    var isDailyPushEnabled: Boolean = isDailyPushEnabled
        protected set

    fun enableDailyPush() {
        isDailyPushEnabled = true
    }

    fun disableDailyPush() {
        isDailyPushEnabled = false
    }

    companion object {
        fun of(user: User, isDailyPushEnabled: Boolean) = UserPreference(user, isDailyPushEnabled)
    }
}