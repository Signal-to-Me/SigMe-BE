package com.sigme.be.auth.exception

import com.sigme.be.global.exception.ErrorCode

class PrimaryProviderAlreadyExistsException(
    message: String? = null
) : AuthException(ErrorCode.PRIMARY_PROVIDER_ALREADY_EXISTS, message) {
}
