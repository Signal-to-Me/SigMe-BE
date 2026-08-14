package com.sigme.be.auth.service

import com.sigme.be.auth.dto.request.EmailLoginRequest
import com.sigme.be.auth.dto.response.EmailLoginResponse
import com.sigme.be.global.properties.JwtProperties
import com.sigme.be.global.security.jwt.IssuedTokenPair
import com.sigme.be.global.security.jwt.JwtTokenService
import org.springframework.stereotype.Service

@Service
class EmailAuthService(
    private val jwtTokenService: JwtTokenService,
    private val jwtProperties: JwtProperties,
    private val authenticationService: EmailPasswordAuthenticationService
) {

    fun login(request: EmailLoginRequest): EmailLoginResponse {
        val tokenPair = issueTokenPairByEmailPassword(request)
        return EmailLoginResponse(
            tokenPair.accessToken,
            jwtProperties.accessTokenTtl.seconds
        )
    }


    fun issueTokenPairByEmailPassword(request: EmailLoginRequest): IssuedTokenPair {
        val userId = authenticationService.authenticate(request)
        return jwtTokenService.issueTokenPair(userId)
    }
}