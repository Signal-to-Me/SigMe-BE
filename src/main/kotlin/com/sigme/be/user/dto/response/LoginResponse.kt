package com.sigme.be.user.dto.response

import java.time.Duration

data class LoginResponse(
    val accessToken: String,
    val expiresInSec: Duration,
)
