package com.sigme.be.psychiatric.entity

import com.sigme.be.global.entity.BaseEntity
import com.sigme.be.user.entity.User
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "psychiatric_visit",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_psy_user_visit_date",
            columnNames = ["user_id", "visit_date"]
        )
    ]
)
class PsychiatricVisit private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    val user: User,

    @Column(name = "visit_date", nullable = false, updatable = false)
    val visitDate: LocalDate,
) : BaseEntity() {
    companion object {
        fun create(
            user: User,
            visitDate: LocalDate
        ) = PsychiatricVisit(user, visitDate)
    }
}