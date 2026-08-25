package com.sigme.be.global.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    val entityId: UUID? = null

    val id: UUID
        get() = checkNotNull(entityId)

    @CreatedDate
    var createdAt: Instant? = null
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseEntity) return false
        if (Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        return entityId != null && entityId == other.entityId
    }

    override fun hashCode(): Int = Hibernate.getClass(this).hashCode()
}