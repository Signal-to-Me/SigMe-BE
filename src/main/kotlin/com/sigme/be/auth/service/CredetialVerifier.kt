package com.sigme.be.auth.service

import com.sigme.be.auth.security.AuthenticationPrincipal
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
class CredetialVerifier(
    private val authenticationManager: AuthenticationManager
) {
    fun authenticate(providerAccountId: String, password: String): UUID {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                providerAccountId,
                password,
            )
        )

        val principal = authentication.principal as AuthenticationPrincipal

        return principal.userId
    }
}