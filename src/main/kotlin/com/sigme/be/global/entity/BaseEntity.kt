package com.sigme.be.global.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.hibernate.Hibernate
import java.time.Instant
import java.util.UUID

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null

    @CreatedDate
    var createdAt: Instant? = null
        protected set

    @LastModifiedDate
    var updatedAt: Instant? = null
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity) return false
        if (Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        return id != null && id == other.id
    }

    override fun hashCode(): Int = Hibernate.getClass(this).hashCode()
}
