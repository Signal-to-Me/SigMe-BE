package com.sigme.be.global.security.jwt

import com.sigme.be.auth.exception.InvalidTokenException
import com.sigme.be.global.properties.JwtProperties
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

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
                ?: throw InvalidTokenException()
            val issuedAt = claims.issuedAt
                ?: throw InvalidTokenException()
            val expiration = claims.expiration
                ?: throw InvalidTokenException()
            claims.id
                ?.takeIf { it.isNotBlank() }
                ?: throw InvalidTokenException()

            if(!expiration.after(issuedAt)) {
                throw InvalidTokenException()
            }

            return UUID.fromString(subject)
        } catch(e : JwtException) {
            throw InvalidTokenException("올바르지 않은 토큰입니다.")
        } catch(e : IllegalArgumentException) {
            throw InvalidTokenException("잘못된 토큰 값입니다.")
        }
    }
}
