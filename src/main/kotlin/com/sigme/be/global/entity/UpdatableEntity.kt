package com.sigme.be.global.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@MappedSuperclass
abstract class UpdatableEntity: BaseEntity() {

    @Column(nullable = false)
    @LastModifiedDate
    var updatedAt: Instant = Instant.now()
        protected set
}
