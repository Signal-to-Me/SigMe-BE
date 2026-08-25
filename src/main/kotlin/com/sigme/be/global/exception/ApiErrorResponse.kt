package com.sigme.be.global.exception

import org.springframework.http.HttpStatusCode
import java.time.Instant

class ApiErrorResponse (
    val code: String,
    val message: String,
    val status: Int,
    val details: Map<String, Any?>,
    val occurredAt: Instant
) {
    companion object {
        /**
         * 오류 응답 본문을 만든다.
         *
         * status는 errorCode가 정한 상태를 기본값으로 쓴다.
         * 프레임워크가 상태를 이미 결정한 경우에만 그 값을 넘겨,
         * 응답 헤더의 상태와 본문의 status가 갈리지 않게 한다.
         */
        fun from(
            errorCode: ErrorCode,
            details: Map<String, Any?> = emptyMap(),
            status: HttpStatusCode = errorCode.httpStatus,
            occurredAt: Instant = Instant.now(),
        ) : ApiErrorResponse = ApiErrorResponse(
            errorCode.code,
            errorCode.message,
            status.value(),
            details,
            occurredAt
        )
    }
}
