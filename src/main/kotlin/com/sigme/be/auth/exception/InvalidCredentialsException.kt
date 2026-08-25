package com.sigme.be.auth.exception

import com.sigme.be.global.exception.ErrorCode

class InvalidCredentialsException(
    cause: Throwable?
) : AuthException(
    ErrorCode.INVALID_CREDENTIALS,
    cause = cause
)