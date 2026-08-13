package com.sigme.be.auth.security

import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

/**
 * Principal은 Authentication에 담길 대상을 나타냄
 */
class EmailPasswordPrincipal (
    val userId: UUID,
    private val email: String,
    passwordHash: String,
    private val enabled: Boolean,
) : UserDetails, CredentialsContainer{ // CC: 인증이 끝난 뒤 비밀번호를 지우는 계약
    private var passwordHash: String? = passwordHash

    override fun getUsername(): String = email

    override fun getPassword(): String? = passwordHash

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun isEnabled(): Boolean = enabled

    override fun eraseCredentials() {
        passwordHash = null
    }
}