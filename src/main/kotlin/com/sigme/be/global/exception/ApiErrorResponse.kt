package com.sigme.be.global.exception

import java.time.Instant

class ApiErrorResponse (
    val code: String,
    val message: String,
    val status: Int,
    val details: Map<String, Any?>,
    val occurredAt: Instant
) {
    companion object {
        fun from(
            errorCode: ErrorCode,
            details: Map<String, Any?> = emptyMap(),
            occurredAt: Instant = Instant.now(),
        ) : ApiErrorResponse = ApiErrorResponse(
            errorCode.code,
            errorCode.message,
            errorCode.httpStatus.value(),
            details,
            occurredAt
        )
    }
}
