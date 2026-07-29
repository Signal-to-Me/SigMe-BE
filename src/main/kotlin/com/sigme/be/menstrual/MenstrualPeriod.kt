package com.sigme.be.menstrual

import com.sigme.be.global.entity.BaseEntity
import com.sigme.be.user.entity.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate

/**
 * 월경 한 번의 기록
 */
@Entity
@Table(name = "menstrual_period")
class MenstrualPeriod private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    val user: User,

    @Column(name = "start_date", nullable = false, updatable = false)
    val menstrualStartDate: LocalDate,

    durationDays: Int
) : BaseEntity() {

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
        fun of(
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
