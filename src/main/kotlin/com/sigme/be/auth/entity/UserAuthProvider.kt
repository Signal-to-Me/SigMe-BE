package com.sigme.be.auth.entity

import com.sigme.be.auth.enums.ProviderType
import com.sigme.be.global.entity.BaseEntity
import com.sigme.be.user.entity.User
import jakarta.persistence.*

@Entity
@Table(
    name = "user_auth_provider",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_auth_provider_account",
            columnNames = ["user_id", "provider_type", "provider_account_id"]
        ),
    ]
)
class UserAuthProvider private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false, updatable = false)
    val providerType: ProviderType,

    @Column(name = "provider_account_id", nullable = false, updatable = false)
    val providerAccountId: String,

    passwordHash: String?,
    isPrimary: Boolean
) : BaseEntity() {

    @Column(name = "password_hash")
    var passwordHash: String? = passwordHash
        protected set

    @Column(name = "is_primary", nullable = false)
    var isPrimary: Boolean = isPrimary
        protected set

    fun updatePasswordHash(passwordHash: String) {
        check(providerType == ProviderType.EMAIL) {
            "이메일 로그인만 비밀번호를 변경할 수 있습니다."
        }

        this.passwordHash = passwordHash
    }

    fun markAsPrimary() {
        check(!isPrimary) {
            "이미 주요 로그인 수단입니다."
        }

        isPrimary = true
    }

    fun unmarkAsPrimary() {
        check(isPrimary) {
            "이미 주요 로그인 수단이 아닙니다."
        }

        isPrimary = false
    }

    companion object {
        fun social(
            user: User,
            providerType: ProviderType,
            providerAccountId: String,
            isPrimary: Boolean
        ): UserAuthProvider {
            require(providerType != ProviderType.EMAIL) {
                "소셜 로그인에는 EMAIL 제공자를 사용할 수 없습니다."
            }

            return UserAuthProvider(
                user,
                providerType,
                providerAccountId,
                null,
                isPrimary
            )
        }

        fun email(
            user: User,
            providerAccountId: String,
            passwordHash: String,
            isPrimary: Boolean
        ) = UserAuthProvider(
            user,
            ProviderType.EMAIL,
            providerAccountId,
            passwordHash,
            isPrimary
        )
    }
}
