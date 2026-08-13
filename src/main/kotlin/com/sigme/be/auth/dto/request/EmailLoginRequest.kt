package com.sigme.be.auth.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class EmailLoginRequest(
    @field:Email @field:NotBlank
    val email: String,
    @field:NotBlank
    val password: String,
)
