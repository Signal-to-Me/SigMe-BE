package com.sigme.be.global.security.jwt

data class IssuedTokenPair(
    val accessToken: String,
    val refreshToken: String,
    val refreshTokenExpiresInSec: Long,
){
    override fun toString(): String {
        return "IssuedTokenPair(accessToken='$accessToken', refreshToken='$refreshToken', refreshTockenExpiresInSec=$refreshTokenExpiresInSec)"
    }
}
