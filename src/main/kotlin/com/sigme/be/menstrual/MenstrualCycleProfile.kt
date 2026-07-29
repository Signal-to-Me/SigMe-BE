package com.sigme.be.menstrual

import com.sigme.be.global.entity.BaseEntity
import com.sigme.be.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "menstrual_cycle_profile")
class MenstrualCycleProfile private constructor(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, unique = true)
    val user: User,

    averageCycleLength: Int,
    averagePeriod: Int
) : BaseEntity() {

    @Column(name = "average_cycle_length_days", nullable = false)
    var averageCycleLength: Int = averageCycleLength
        protected set

    @Column(name = "average_period_duration_days", nullable = false)
    var averagePeriod: Int = averagePeriod
        protected set

    fun updateCycleLengthDays(cycleLength: Int) {
        validateCycle(cycleLength, averagePeriod)
        averageCycleLength = cycleLength
    }

    fun updateAveragePeriod(period: Int) {
        validateCycle(averageCycleLength, period)
        averagePeriod = period
    }

    init {
        validateCycle(averageCycleLength, averagePeriod)
    }

    companion object {
        fun of(
            user: User,
            averageCycleLength: Int,
            averagePeriod: Int
        ) = MenstrualCycleProfile(user, averageCycleLength, averagePeriod)

        private fun validateCycle(cycleLength: Int, period: Int) {
            require(cycleLength > 1) {
                "월경 주기는 2일 이상이어야 합니다."
            }
            require(period > 0) {
                "월경 기간은 1일 이상이어야 합니다."
            }
            require(period < cycleLength) {
                "월경 주기는 월경 기간보다 길어야 합니다."
            }
        }
    }
}
