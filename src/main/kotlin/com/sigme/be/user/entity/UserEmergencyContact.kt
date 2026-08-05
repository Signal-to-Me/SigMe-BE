package com.sigme.be.user.entity

import com.sigme.be.global.entity.UpdatableEntity
import jakarta.persistence.*

@Entity(name = "emergency_contact")
class UserEmergencyContact private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    val user: User,

    name: String,

    contactNumber: String
) : UpdatableEntity() {

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var contactNumber = contactNumber
        protected set

    init{
        require(name.isNotBlank()) {
            "이름이 비어있을 수 없습니다."
        }
        require(contactNumber.isNotBlank()){
            "전화번호가 비어있을 수 없습니다."
        }
    }

    companion object{
        fun of(user: User, name: String, contactNumber: String) = UserEmergencyContact(user, name, contactNumber)
    }
}