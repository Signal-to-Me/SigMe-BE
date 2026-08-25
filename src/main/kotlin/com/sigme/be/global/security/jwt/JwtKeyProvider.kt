package com.sigme.be.global.security.jwt

import com.sigme.be.global.properties.JwtProperties
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import javax.crypto.SecretKey

@Component
class JwtKeyProvider(
    properties: JwtProperties
) {
    val secretKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secretKey))
}