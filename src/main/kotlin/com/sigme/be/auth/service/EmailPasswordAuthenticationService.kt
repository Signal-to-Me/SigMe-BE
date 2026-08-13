package com.sigme.be.auth.service

import com.sigme.be.auth.dto.request.EmailLoginRequest
import com.sigme.be.auth.security.EmailPasswordPrincipal
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailPasswordAuthenticationService(
    private val authenticationManager: AuthenticationManager
) {
    fun authenticate(request: EmailLoginRequest): UUID {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                request.email,
                request.password
            )
        )

        val principal = authentication.principal as EmailPasswordPrincipal

        return principal.userId
    }
}