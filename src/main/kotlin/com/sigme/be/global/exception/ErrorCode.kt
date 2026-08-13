package com.sigme.be.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

/**
 * 애플리케이션 전역 에러 코드.
 *
 * code는 `{도메인}-{일련번호}` 형식을 따른다.
 * 도메인은 패키지명을 대문자로 옮긴 값이며(AUTH, USER, BINGO, HABIT, MEDICATION 등),
 * 특정 도메인에 속하지 않는 항목은 COMMON을 사용한다.
 * 일련번호는 도메인별로 001부터 부여하고, 한번 배포된 code는 재사용하지 않는다.
 */
enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatusCode
) {

    // 공통 (COMMON)
    INVALID_REQUEST("COMMON-001", "요청 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("COMMON-002", "내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("COMMON-003", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    companion object {
        fun from(status: HttpStatusCode): ErrorCode = entries.firstOrNull{it.httpStatus == status} ?: INTERNAL_SERVER_ERROR
    }
}