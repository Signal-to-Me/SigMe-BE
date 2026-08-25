package com.sigme.be.global.exception

abstract class SigmeException(
    val errorCode: ErrorCode,
    details: Map<String, Any?> = emptyMap(),
    cause: Throwable? = null,
): RuntimeException(errorCode.message, cause) {
    val details: Map<String, Any?> = details.toMap()
}