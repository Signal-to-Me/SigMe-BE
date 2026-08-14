package com.sigme.be.user.dto.request

import com.sigme.be.auth.enums.ProviderType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SocialSignUpRequest(
    @field:NotNull
    val providerType: ProviderType,

    @field:NotBlank
    val providerAccountId: String,

    @field:NotBlank
    val userName: String,

    @field:NotNull
    val isPrimary: Boolean
) {
    override fun toString(): String {
        return "SocialSignUpRequest(providerType=$providerType, userName='$userName', isPrimary=$isPrimary)"
    }
}
