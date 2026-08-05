package com.sigme.be.medication.entity

import com.sigme.be.global.entity.BaseEntity
import com.sigme.be.medication.enums.IntakePeriod
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDate
import java.util.*

@Entity
class MedicationIntakeCheckHistory private constructor(

    @Column(nullable = false, updatable = false)
    val userId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val intakePeriod: IntakePeriod,

    @Column(nullable = false, updatable = false)
    val intakeDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val type: Action
) : BaseEntity() {
    companion object{
        fun create(userId: UUID, source: MedicationIntakeCheck, type: Action) = MedicationIntakeCheckHistory(
            userId,
            source.intakePeriod,
            source.intakeDate,
            type
        )
    }

    enum class Action{CHECK, UNCHECK}
}