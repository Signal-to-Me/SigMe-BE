package com.sigme.be.bingo.entity

import com.sigme.be.global.entity.UpdatableEntity
import com.sigme.be.user.entity.User
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(
            "uk_daily_bingo_board_user_board_date",
            ["user_id", "boardDate"]
        )
    ]
)
class DailyBingoBoard private constructor(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    val user: User,

    @Column(nullable = false, updatable = false)
    val boardDate: LocalDate,

) : UpdatableEntity() {

    @Column(nullable = false)
    var isCompleted = false
        protected set

    @Column(nullable = false)
    var rerollCount = 0
        protected set

    fun reroll() = rerollCount++;
    fun complete() {
        check(!isCompleted) {
            "이미 완료된 빙고입니다."
        }
        isCompleted = true
    }

    fun unComplete() {
        check(isCompleted) {
            "아직 완료되지 않은 빙고입니다."
        }
        isCompleted = false
    }

    companion object {
        fun create(
            user: User,
            boardDate: LocalDate
        ) = DailyBingoBoard(user, boardDate)
    }

}