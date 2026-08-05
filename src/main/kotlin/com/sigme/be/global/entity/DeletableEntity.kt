package com.sigme.be.global.entity

import jakarta.persistence.MappedSuperclass
import java.time.Instant

@MappedSuperclass
abstract class DeletableEntity : UpdatableEntity() {
    var deletedAt: Instant? = null
        protected set

    fun delete(deletedAt: Instant) {
        check(this.deletedAt == null) {
            "이미 삭제된 데이터입니다."
        }

        this.deletedAt = deletedAt
    }
}
