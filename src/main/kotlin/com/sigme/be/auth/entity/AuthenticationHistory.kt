package com.sigme.be.auth.entity

import com.sigme.be.auth.enums.AuthenticationAction
import com.sigme.be.auth.enums.ProviderType
import com.sigme.be.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.util.UUID

@Entity
class AuthenticationHistory private constructor(
    @Column(nullable = false, updatable = false)
    val userId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val action: AuthenticationAction,

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    val providerType: ProviderType?
) : BaseEntity() {
    companion object {
        fun create(
            userId: UUID,
            action: AuthenticationAction,
            providerType: ProviderType? = null
        ) = AuthenticationHistory(
            userId = userId,
            action = action,
            providerType = providerType
        )
    }
}
