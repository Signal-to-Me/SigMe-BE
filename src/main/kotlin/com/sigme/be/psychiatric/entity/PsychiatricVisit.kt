package com.sigme.be.psychiatric.entity

import com.sigme.be.global.entity.UpdatableEntity
import com.sigme.be.user.entity.User
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(
    name = "psychiatric_visit",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_psy_user_visit_date_time",
            columnNames = ["user_id", "visit_date", "visit_time"]
        )
    ]
)
class PsychiatricVisit private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    val user: User,

    @Column(name = "visit_date", nullable = false, updatable = false)
    val visitDate: LocalDate,

    visitTime: LocalTime
) : UpdatableEntity() {

    @Column(name = "visit_time", nullable = false)
    var visitTime: LocalTime = normalize(visitTime)
        protected set

    fun updateVisitTime(visitTime: LocalTime) {
        this.visitTime = normalize(visitTime)
    }

    companion object {
        fun create(
            user: User,
            visitDate: LocalDate,
            visitTime: LocalTime
        ) = PsychiatricVisit(user, visitDate, visitTime)

        private fun normalize(visitTime: LocalTime) =
            visitTime.withSecond(0).withNano(0)
    }
}