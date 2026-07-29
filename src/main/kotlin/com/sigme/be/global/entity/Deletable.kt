package com.sigme.be.global.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.Instant

@MappedSuperclass
abstract class Deletable : BaseEntity() {

    @Column(name = "deleted_at")
    var deletedAt: Instant? = null
        protected set

    fun delete(deletedAt: Instant) {
        check(this.deletedAt == null) {
            "이미 삭제된 데이터입니다."
        }

        this.deletedAt = deletedAt
    }
}
