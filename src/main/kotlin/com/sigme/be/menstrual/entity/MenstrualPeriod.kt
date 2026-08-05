package com.sigme.be.menstrual.entity

import com.sigme.be.global.entity.UpdatableEntity
import com.sigme.be.user.entity.User
import jakarta.persistence.*
import java.time.LocalDate

/**
 * 월경 한 번의 기록
 */
@Entity
@Table(uniqueConstraints = [
    UniqueConstraint("uk_period_user_start_date",
        ["user_id", "start_date"])
])
class MenstrualPeriod private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    val user: User,

    @Column(name = "start_date", nullable = false, updatable = false)
    val menstrualStartDate: LocalDate,

    durationDays: Int
) : UpdatableEntity() {

    @Column(name = "duration_days", nullable = false)
    var durationDays: Int = durationDays
        protected set

    init {
        validateDurationDays(durationDays)
    }

    fun updateDurationDays(durationDays: Int) {
        validateDurationDays(durationDays)
        this.durationDays = durationDays
    }

    companion object {
        fun create(
            user: User,
            startDate: LocalDate,
            durationDays: Int
        ) = MenstrualPeriod(user, startDate, durationDays)

        private fun validateDurationDays(durationDays: Int) {
            require(durationDays > 0) {
                "월경 기간은 1일 이상이어야 합니다."
            }
        }
    }
}
