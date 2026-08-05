package com.sigme.be.bingo.entity

import com.sigme.be.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.util.*

@Entity
class BingoItemReplacementHistory private constructor(

    @Column(nullable = false, updatable = false)
    val userId: UUID,

    @Column(nullable = false, updatable = false)
    val dailyBingoBoardId: UUID,

    @Column(nullable = false, updatable = false)
    val cellPosition: Int,

    @Column(nullable = false, updatable = false)
    val previousBingoItemId: UUID,

    @Column(nullable = false, updatable = false)
    val previousContent: String,

    @Column(nullable = false, updatable = false)
    val previousIsHighlighted: Boolean,

    @Column(nullable = false, updatable = false)
    val replacementBingoItemId: UUID,

    @Column(nullable = false, updatable = false)
    val replacementContent: String,

    @Column(nullable = false, updatable = false)
    val replacementIsHighlighted: Boolean
) : BaseEntity() {
    companion object {
        fun of(
            userId: UUID,
            source: DailyBingoCheck,
            replacement: BingoItem
        ) = BingoItemReplacementHistory(
            userId = userId,
            dailyBingoBoardId = requireNotNull(source.dailyBingoBoard.id) {
                "저장된 빙고판만 교체 이력을 생성할 수 있습니다."
            },
            cellPosition = source.cellPosition,
            previousBingoItemId = requireNotNull(source.bingoItem.id) {
                "저장된 빙고 항목만 교체 이력을 생성할 수 있습니다."
            },
            previousContent = source.contentSnapshot,
            previousIsHighlighted = source.isHighlightedSnapshot,
            replacementBingoItemId = requireNotNull(replacement.id) {
                "저장된 빙고 항목으로만 교체할 수 있습니다."
            },
            replacementContent = replacement.content,
            replacementIsHighlighted = replacement.isHighlighted
        )
    }
}
