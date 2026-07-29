package com.sigme.be.user.entity

import com.sigme.be.global.entity.Deletable
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity(name = "app_user")
class User(
    userName: String
) : Deletable() {
    @Column(nullable = false)
    var userName: String = userName
        protected set

    companion object {
        fun of(userName: String) = User(userName)
    }
}