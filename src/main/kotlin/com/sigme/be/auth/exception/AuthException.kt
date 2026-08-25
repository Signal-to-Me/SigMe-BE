package com.sigme.be.auth.exception

import com.sigme.be.global.exception.ErrorCode
import com.sigme.be.global.exception.SigmeException

abstract class AuthException(
    errorCode: ErrorCode,
    message: String? = null,
    details: Map<String, Any?> = emptyMap(),
    cause: Throwable? = null,
) : SigmeException(
    errorCode,
    details = details + buildMap {
        put("domain", "Auth")
        message?.let { put("message", it) }
    },
    cause = cause
) {
}