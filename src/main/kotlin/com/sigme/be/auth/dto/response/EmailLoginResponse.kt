package com.sigme.be.auth.dto.response

data class EmailLoginResponse(
    val accessToken: String,
    val expiresInSec: Long,
    val tokenType: String = "Bearer",
) {
}
