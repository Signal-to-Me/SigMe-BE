package com.sigme.be.auth.exception

import com.sigme.be.global.exception.ErrorCode

class DuplicateAuthProviderException() : AuthException(
    errorCode = ErrorCode.DUPLICATE_AUTH_PROVIDER
)
