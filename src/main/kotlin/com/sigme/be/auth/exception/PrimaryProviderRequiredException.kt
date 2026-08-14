package com.sigme.be.auth.exception

import com.sigme.be.global.exception.ErrorCode

class PrimaryProviderRequiredException(
    message: String? = null
) : AuthException(ErrorCode.PRIMARY_PROVIDER_REQUIRED, message) {
}
