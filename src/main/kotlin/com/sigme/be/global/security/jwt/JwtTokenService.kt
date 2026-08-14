package com.sigme.be.global.security.jwt

import com.sigme.be.auth.exception.InvalidTokenException
import com.sigme.be.global.properties.JwtProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

private val log = KotlinLogging.logger { }

@Component
class JwtTokenService(
    private val jwtProperties: JwtProperties,
    keyProvider: JwtKeyProvider
) {
    private val secretKey: SecretKey = keyProvider.secretKey

    private val parser = Jwts.parser()
        .verifyWith(secretKey)
        .requireIssuer(jwtProperties.issuer)
        .build()

    fun issueTokenPair(accountId: UUID): IssuedTokenPair {
        val issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val refreshExpiresAt = issuedAt.plus(jwtProperties.refreshTokenTtl)

        return IssuedTokenPair(
            accessToken = issue(
                accountId,
                TokenType.ACCESS,
                issuedAt,
                jwtProperties.accessTokenTtl
            ),
            refreshToken = issue(
                accountId,
                TokenType.REFRESH,
                issuedAt,
                jwtProperties.refreshTokenTtl
            ),
            refreshTokenExpiresAt = refreshExpiresAt
        )
    }

    private fun issue(
        accountId: UUID,
        type: TokenType,
        issuedAt: Instant,
        ttl: Duration
    ): String {
        val expiresAt = issuedAt.plus(ttl)
        return Jwts.builder()
            .id(UUID.randomUUID().toString())
            .subject(accountId.toString())
            .issuer(jwtProperties.issuer)
            .issuedAt(Date.from(issuedAt))
            .expiration(Date.from(expiresAt))
            .claim("type", type.name)
            .signWith(secretKey)
            .compact()

    }

    /**
     * 검증 실패 원인은 로그에만 남기고 밖으로는 하나의 예외로 통일한다.
     * 원인을 응답에 노출하면 토큰을 조립해가며 무엇이 맞는지 좁힐 수 있다.
     */
    private fun rejectToken(expected: TokenType, reason: String): Nothing {
        log.debug { "토큰 검증 실패 expected=$expected reason=$reason" }
        throw InvalidTokenException("올바르지 않은 토큰입니다.")
    }

    fun parseAccessToken(token: String): UUID =
        parseSubject(token, TokenType.ACCESS)

    fun parseRefreshToken(token: String): UUID =
        parseSubject(token, TokenType.REFRESH)

    fun parseSubject(
        token: String,
        type: TokenType
    ): UUID {
        try {
            val claims = parser.parseSignedClaims(token).payload

            val subject = claims.subject
                ?.takeIf { it.isNotBlank() }
                ?: rejectToken(type, "subject 누락")
            val issuedAt = claims.issuedAt
                ?: rejectToken(type, "issuedAt 누락")
            val expiration = claims.expiration
                ?: rejectToken(type, "expiration 누락")
            claims.id
                ?.takeIf { it.isNotBlank() }
                ?: rejectToken(type, "jti 누락")

            if (!expiration.after(issuedAt)) {
                rejectToken(type, "만료 시각이 발급 시각보다 앞섬")
            }

            val actualType = claims["type"] as? String
            if (actualType != type.name) {
                rejectToken(type, "토큰 종류 불일치 actual=$actualType")
            }

            return UUID.fromString(subject)
        } catch (e: JwtException) {
            // 서명·만료·형식 오류가 모두 여기로 온다. 원인은 로그에만 남긴다.
            log.debug(e) { "토큰 검증 실패 expected=$type reason=${e.javaClass.simpleName}" }
            throw InvalidTokenException("올바르지 않은 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            log.debug(e) { "토큰 값 해석 실패 expected=$type" }
            throw InvalidTokenException("잘못된 토큰 값입니다.")
        }
    }
}
