package com.sigme.be.user.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class EmailSignUpRequest(
    @field:Email @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String,

    @field:NotBlank
    val userName: String,

    @field:NotNull
    val isPrimary: Boolean
) {
    override fun toString(): String {
        return "EmailSignUpRequest(userName='$userName', isPrimary=$isPrimary)"
    }
}
