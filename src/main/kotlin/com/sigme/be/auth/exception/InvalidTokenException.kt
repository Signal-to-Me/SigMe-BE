package com.sigme.be.auth.exception

import com.sigme.be.global.exception.ErrorCode

class InvalidTokenException(
    message: String? = null
) : AuthException(ErrorCode.INVALID_TOKEN, message) {
}