package com.sigme.be.auth.service

import com.sigme.be.auth.entity.AuthenticationHistory
import com.sigme.be.auth.entity.UserAuthProvider
import com.sigme.be.auth.enums.AuthenticationAction
import com.sigme.be.auth.enums.ProviderType
import com.sigme.be.auth.exception.DuplicateAuthProviderException
import com.sigme.be.auth.exception.PrimaryProviderAlreadyExistsException
import com.sigme.be.auth.exception.PrimaryProviderRequiredException
import com.sigme.be.auth.repository.AuthenticationHistoryRepository
import com.sigme.be.auth.repository.UserAuthProviderRepository
import com.sigme.be.global.properties.JwtProperties
import com.sigme.be.global.security.jwt.IssuedTokenPair
import com.sigme.be.global.security.jwt.JwtTokenService
import com.sigme.be.user.entity.User
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

private val log = KotlinLogging.logger { }


@Service
class AuthService(
    private val jwtTokenService: JwtTokenService,
    private val jwtProperties: JwtProperties,
    private val credentialVerifier: CredentialVerifier,
    private val userAuthProviderRepository: UserAuthProviderRepository,
    private val authenticationHistoryRepository: AuthenticationHistoryRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun linkEmailProvider(
        user: User,
        email: String,
        password: String,
        isPrimary: Boolean = false
    ) {
        val userId = checkNotNull(user.id)
        checkValid(user, ProviderType.EMAIL, email, isPrimary)
        saveAuthenticationProvider(user, email, password, isPrimary)

        // 링크 이력을 남김
        saveAuthenticationHistory(ProviderType.EMAIL, userId)

        log.info { "인증 수단 연결 userId=$userId provider=${ProviderType.EMAIL} primary=$isPrimary" }
    }

    fun authenticate(email: String, password: String): IssuedTokenPair {
        val userId = credentialVerifier.authenticate(email, password)
        val tokenPair = jwtTokenService.issueTokenPair(userId)

        log.info { "로그인 성공 userId=$userId" }

        return tokenPair
    }

    private fun checkValid(
        user: User,
        providerType: ProviderType,
        providerAccountId: String,
        isPrimary: Boolean
    ) {
        checkValidProviderId(providerType, providerAccountId)

        val userAuthProviders = userAuthProviderRepository.findByUser(user)

        checkPrimaryValid(userAuthProviders, isPrimary)
        checkNotDuplicated(userAuthProviders, providerType, providerAccountId)
    }

    private fun checkValidProviderId(providerType: ProviderType, providerAccountId: String) {
        userAuthProviderRepository.findByProviderTypeAndProviderAccountId(providerType, providerAccountId)
            ?.let { throw DuplicateAuthProviderException() }

    }

    private fun checkNotDuplicated(
        userAuthProviders: List<UserAuthProvider>,
        providerType: ProviderType,
        providerAccountId: String
    ) {
        if (userAuthProviders.any { it.providerType == providerType && it.providerAccountId == providerAccountId })
            throw DuplicateAuthProviderException()
    }

    private fun checkPrimaryValid(
        userAuthProviders: List<UserAuthProvider>,
        isPrimary: Boolean
    ) {
        val hasPrimary = userAuthProviders.any(UserAuthProvider::isPrimary)

        if (!hasPrimary && !isPrimary)
            throw PrimaryProviderRequiredException()
        if (hasPrimary && isPrimary)
            throw PrimaryProviderAlreadyExistsException()
    }

    private fun saveAuthenticationHistory(providerType: ProviderType, userId: UUID) =
        authenticationHistoryRepository.save(
            AuthenticationHistory.create(
                userId = userId,
                action = AuthenticationAction.AUTH_PROVIDER_LINKED,
                providerType = providerType
            )
        )


    private fun saveAuthenticationProvider(
        user: User,
        email: String,
        password: String,
        isPrimary: Boolean
    ) {
        val passwordHash = checkNotNull(passwordEncoder.encode(password))

        userAuthProviderRepository.save(
            UserAuthProvider.email(
                user = user,
                email = email,
                passwordHash = passwordHash,
                isPrimary = isPrimary,
            )
        )
    }
}