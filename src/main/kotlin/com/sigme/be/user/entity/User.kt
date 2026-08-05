package com.sigme.be.user.entity

import com.sigme.be.global.entity.DeletableEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "app_user")
class User(
    userName: String
) : DeletableEntity() {
    @Column(nullable = false)
    var userName: String = userName
        protected set

    companion object {
        fun of(userName: String) = User(userName)
    }
}