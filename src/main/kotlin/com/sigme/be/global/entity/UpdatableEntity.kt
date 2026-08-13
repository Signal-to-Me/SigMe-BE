package com.sigme.be.global.entity

import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@MappedSuperclass
abstract class UpdatableEntity: BaseEntity() {
    @LastModifiedDate
    var updatedAt: Instant? = null
        protected set
}
