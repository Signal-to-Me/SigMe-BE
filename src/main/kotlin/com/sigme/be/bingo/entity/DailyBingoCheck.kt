package com.sigme.be.bingo.entity

import com.sigme.be.global.entity.UpdatableEntity
import jakarta.persistence.*

@Entity
@Table(uniqueConstraints = [
    UniqueConstraint("uk_daily_bingo_check_board_item",
        ["daily_bingo_board_id", "bingo_item_id"]),
    UniqueConstraint("uk_daily_bingo_check_board_item_content",
        ["daily_bingo_board_id", "content_snapshot"]),
    UniqueConstraint("uk_daily_bingo_board_cell_position",
        ["daily_bingo_board_id", "cell_position"])
])
class DailyBingoCheck private constructor(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_bingo_board_id", nullable = false, updatable = false)
    val dailyBingoBoard: DailyBingoBoard,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bingo_item_id", nullable = false, updatable = false)
    val bingoItem: BingoItem,

    @Column(nullable = false, updatable = false)
    val contentSnapshot: String,

    @Column(nullable = false, updatable = false)
    val isHighlightedSnapshot: Boolean,

    @Column(nullable = false, updatable = false)
    val cellPosition: Int,

    @Column(nullable = false, updatable = false)
    val isReplacement: Boolean = false
) : UpdatableEntity() {

    @Column(nullable = false)
    var isChecked = false
        protected set

    fun check() {
        check(!isChecked) {
            "이미 체크되었습니다."
        }
        isChecked = true
    }

    fun uncheck() {
        check(isChecked) {
            "이미 체크 해제되어있습니다."
        }
        isChecked = false
    }

    init {
        require(cellPosition in 0..8) {
            "빙고 칸 위치는 0에서 8 사이여야 합니다."
        }
    }

    companion object{
        fun create(
            dailyBingoBoard: DailyBingoBoard,
            bingoItem: BingoItem,
            cellPosition: Int
        ) = DailyBingoCheck(
            dailyBingoBoard = dailyBingoBoard,
            bingoItem = bingoItem,
            contentSnapshot = bingoItem.content,
            isHighlightedSnapshot = bingoItem.isHighlighted,
            cellPosition = cellPosition)

        fun replaced(
            dailyBingoBoard: DailyBingoBoard,
            bingoItem: BingoItem,
            cellPosition: Int
        ) = DailyBingoCheck(
            dailyBingoBoard = dailyBingoBoard,
            bingoItem = bingoItem,
            contentSnapshot = bingoItem.content,
            isHighlightedSnapshot = bingoItem.isHighlighted,
            cellPosition = cellPosition,
            isReplacement = true)
    }

}