package com.sigme.be.global.security.jwt

import java.time.Instant

data class IssuedTokenPair(
    val accessToken: String,
    val refreshToken: String,
    val refreshTokenExpiresAt: Instant,
) {
}
