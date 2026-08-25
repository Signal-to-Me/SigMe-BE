package com.sigme.be.auth.service

import com.sigme.be.auth.exception.AuthSystemException
import com.sigme.be.auth.exception.InvalidCredentialsException
import com.sigme.be.auth.security.AuthenticationPrincipal
import org.springframework.security.authentication.*
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import java.util.*

@Service
class CredentialVerifier(
    private val authenticationManager: AuthenticationManager
) {
    fun authenticate(providerAccountId: String, password: String): UUID {
        try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                    providerAccountId,
                    password,
                )
            )

            val principal = authentication.principal as AuthenticationPrincipal

            return principal.userId
        } catch(e: BadCredentialsException) {
            throw InvalidCredentialsException(e)
        } catch (e: AccountStatusException) {
            throw InvalidCredentialsException(e)
        } catch (e: AuthenticationServiceException) {
            throw AuthSystemException(e)
        } catch (e: AuthenticationException) {
            throw AuthSystemException(e)
        }
    }
}