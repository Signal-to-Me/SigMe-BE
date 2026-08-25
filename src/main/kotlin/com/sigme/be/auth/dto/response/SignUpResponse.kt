package com.sigme.be.auth.dto.response

import com.sigme.be.global.security.jwt.IssuedTokenPair
import java.util.*

data class SignUpResponse(
    val userId: UUID,
    val tokenPair: IssuedTokenPair,
    )
