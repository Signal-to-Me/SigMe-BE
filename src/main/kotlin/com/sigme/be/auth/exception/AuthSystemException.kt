package com.sigme.be.auth.exception

import com.sigme.be.global.exception.ErrorCode

class AuthSystemException(cause: Throwable?) : AuthException(
    errorCode = ErrorCode.INTERNAL_SERVER_ERROR,
    cause = cause
) {
}