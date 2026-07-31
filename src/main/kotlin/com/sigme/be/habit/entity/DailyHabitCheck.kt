package com.sigme.be.habit.entity

import com.sigme.be.global.entity.UpdatableEntity
import com.sigme.be.habit.enums.HabitType
import com.sigme.be.user.entity.User
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(uniqueConstraints = [
    UniqueConstraint("uk_daily_habit_check_user_date_type",
        ["user_id", "check_date", "habit_type"])
])
class DailyHabitCheck private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    val user: User,

    @Column(nullable = false, updatable = false)
    val checkDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val habitType: HabitType,
) : UpdatableEntity() {
    @get:Transient
    val isChecked: Boolean
        get() = checkedAt != null

    var checkedAt: Instant? = null
        protected set

    fun check(checkedAt: Instant){
        check(!isChecked){
            "이미 체크된 항목입니다."
        }
        this.checkedAt = checkedAt
    }

    fun uncheck(){
        check(isChecked) {
            "이미 해제된 항목입니다."
        }
        this.checkedAt = null
    }

    companion object {
        fun of(user: User, checkDate: LocalDate, habitType: HabitType) = DailyHabitCheck(user, checkDate, habitType)
    }
}