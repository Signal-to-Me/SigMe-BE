package com.sigme.be.global.exception

data class FieldViolation(
    val field: String,
    val message: String
)
