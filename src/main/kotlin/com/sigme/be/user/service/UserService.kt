package com.sigme.be.user.service

import com.sigme.be.auth.dto.request.EmailLoginRequest
import com.sigme.be.auth.dto.response.SignUpResponse
import com.sigme.be.auth.service.AuthService
import com.sigme.be.global.properties.JwtProperties
import com.sigme.be.global.security.jwt.JwtTokenService
import com.sigme.be.user.dto.request.EmailSignUpRequest
import com.sigme.be.user.dto.response.LoginResponse
import com.sigme.be.user.entity.User
import com.sigme.be.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val authService: AuthService,
    private val jwtTokenService: JwtTokenService,
    private val jwtProperties: JwtProperties
) {
    fun signUp(request: EmailSignUpRequest): SignUpResponse {
        val user = userRepository.save(User.of(request.username))
        authService.linkEmailProvider(
            user = user,
            email = request.email,
            password = request.password,
            isPrimary = request.isPrimary)
        return SignUpResponse(
            userId = user.id,
            tokenPair = jwtTokenService.issueTokenPair(checkNotNull(user.id))
        )
    }

    // TODO signUp SocialSignUpRequest로 오버로드

    fun login(request: EmailLoginRequest) =
        LoginResponse(
            authService.authenticate(request.email, request.password).accessToken,
            jwtProperties.accessTokenTtl
        )


}