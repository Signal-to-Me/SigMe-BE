package com.sigme.be.auth.repository

import com.sigme.be.auth.entity.UserAuthProvider
import com.sigme.be.auth.enums.ProviderType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserAuthProviderRepository : JpaRepository<UserAuthProvider, UUID> {
    fun findByProviderTypeAndProviderAccountId(
        providerType: ProviderType,
        providerAccountId: String
    ): UserAuthProvider?
}