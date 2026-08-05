package com.sigme.be.greeting.entity

import com.sigme.be.global.entity.BaseEntity
import com.sigme.be.greeting.enums.GreetingPeriod
import jakarta.persistence.*
import org.hibernate.annotations.Immutable

@Entity
@Table
@Immutable
class Greeting private constructor(

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    val timePeriod: GreetingPeriod,

    @Column(nullable = false, updatable = false)
    val content: String
) : BaseEntity()