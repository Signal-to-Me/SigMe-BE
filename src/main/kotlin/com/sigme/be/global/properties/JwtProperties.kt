package com.sigme.be.global.properties

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.nio.charset.StandardCharsets
import java.time.Duration

@Validated
@ConfigurationProperties(prefix = "sigme.jwt")
class JwtProperties (
    @field:NotBlank
    val secretKey: String,

    @field:NotBlank
    val issuer: String,

    val accessTokenTtl: Duration,
    val refreshTokenTtl: Duration
){
    init {
        require(secretKey.toByteArray(StandardCharsets.UTF_8).size >= 32) {
            "jwt 비밀 키는 UTF-8 기준으로 32바이트 이상이어야 합니다."
        }
        require(!accessTokenTtl.isZero && !accessTokenTtl.isNegative){
            "엑세스 토큰이 잘못되었습니다."
        }
        require(refreshTokenTtl > accessTokenTtl){
            "리프레시 토큰은 엑세스 토큰보다 길어야합니다."
        }
    }
}