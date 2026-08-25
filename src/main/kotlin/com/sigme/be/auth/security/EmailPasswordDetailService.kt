package com.sigme.be.auth.security

import com.sigme.be.auth.entity.UserAuthProvider
import com.sigme.be.auth.enums.ProviderType
import com.sigme.be.auth.repository.UserAuthProviderRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private const val ACCOUNT_NOT_FOUND_MESSAGE = "이메일 계정을 찾을 수 없습니다."


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
            ?: throw UsernameNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE)

        val passwordHash = provider.passwordHash
            ?: throw UsernameNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE)

        return AuthenticationPrincipal(
            userId = provider.user.id,
            email = provider.providerAccountId,
            passwordHash = passwordHash,
            enabled = provider.user.deletedAt == null
        )
    }
}