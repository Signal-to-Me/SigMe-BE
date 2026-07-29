package com.sigme.be.medication.entity

import com.sigme.be.global.entity.BaseEntity
import com.sigme.be.medication.enums.IntakePeriod
import com.sigme.be.user.entity.User
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDate

@Entity
@Table(
    name = "medication_intake_check",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_medication_user_intake_period_date",
            columnNames = ["user_id", "intake_period", "intake_date"]
        )
    ]
)
class MedicationIntakeCheck private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "intake_period", nullable = false, updatable = false)
    val intakePeriod: IntakePeriod,

    @Column(name = "intake_date", nullable = false, updatable = false)
    val intakeDate: LocalDate
) : BaseEntity() {

    @get:Transient
    val isTaken: Boolean
        get() = checkedAt != null

    @Column(name = "checked_at")
    var checkedAt: Instant? = null
        protected set

    fun check(checkedAt: Instant) {
        this.checkedAt = checkedAt
    }

    fun uncheck() {
        checkedAt = null
    }

    companion object {
        fun of(
            user: User,
            intakePeriod: IntakePeriod,
            intakeDate: LocalDate
        ) = MedicationIntakeCheck(user, intakePeriod, intakeDate)
    }
}
