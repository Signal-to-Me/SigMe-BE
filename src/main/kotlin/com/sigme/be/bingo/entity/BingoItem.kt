package com.sigme.be.bingo.entity

import com.sigme.be.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.hibernate.annotations.Immutable

@Entity
@Immutable
class BingoItem private constructor(
    @Column(nullable = false, unique = true)
    val content: String,

    @Column(nullable = false, updatable = false)
    val isHighlighted: Boolean


): BaseEntity()