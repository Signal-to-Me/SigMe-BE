package com.sigme.be.auth.security

import com.sigme.be.auth.entity.UserAuthProvider
import com.sigme.be.auth.enums.ProviderType
import com.sigme.be.auth.repository.UserAuthProviderRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class EmailPasswordDetailService(
    private val userAuthProviderRepository: UserAuthProviderRepository,
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val provider = userAuthProviderRepository.findByProviderTypeAndProviderAccountId(
            ProviderType.EMAIL,
            UserAuthProvider.emailNormalize(email)
        )
            ?: throw UsernameNotFoundException("이메일 계정을 찾을 수 없습니다.")

        val passwordHash = provider.passwordHash
            ?: throw UsernameNotFoundException("이메일 계정을 찾을 수 없습니다.")

        val userId = checkNotNull(provider.user.id)

        return EmailPasswordPrincipal(
            userId = userId,
            email = provider.providerAccountId,
            passwordHash = passwordHash,
            enabled = provider.user.deletedAt == null,
        )
    }
}