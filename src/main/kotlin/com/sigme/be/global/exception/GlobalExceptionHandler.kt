package com.sigme.be.global.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.method.ParameterErrors
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

private val log = KotlinLogging.logger { }

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(SigmeException::class)
    fun handleSigmeException(
        ex: SigmeException
    ): ResponseEntity<ApiErrorResponse> {
        log.warn { "처리된 예외 code=${ex.errorCode.code} status=${ex.errorCode.httpStatus.value()}" }

        return ResponseEntity
            .status(ex.errorCode.httpStatus)
            .body(
                ApiErrorResponse.from(
                    ex.errorCode,
                    ex.details
                )
            )
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val fieldViolations = ex.bindingResult.fieldErrors
            .map { fieldError ->
                FieldViolation(
                    field = fieldError.field,
                    message = fieldError.defaultMessage.orDefaultValidationMessage()
                )
            }

        val objectViolations = ex.bindingResult.globalErrors
            .map { error ->
                FieldViolation(
                    field = "request",
                    message = error.defaultMessage.orDefaultValidationMessage()
                )
            }

        val violations = (fieldViolations + objectViolations)
            .sortedBy(FieldViolation::field)

        return invalidRequestResponse(violations)
    }

    override fun handleHandlerMethodValidationException(
        ex: HandlerMethodValidationException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        if (ex.isForReturnValue) {
            log.error(ex) { "컨트롤러 반환 값의 검증에 실패했습니다." }
            val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
            return ResponseEntity
                .status(errorCode.httpStatus)
                .body(ApiErrorResponse.from(errorCode))
        }

        val parameterViolations = ex.parameterValidationResults
            .flatMap { result ->
                if (result is ParameterErrors) {
                    val fieldViolations = result.fieldErrors.map { f ->
                        FieldViolation(
                            field = f.field,
                            message = f.defaultMessage.orDefaultValidationMessage()
                        )
                    }
                    val objectViolations = result.globalErrors.map { e ->
                        FieldViolation(
                            field = "request",
                            message = e.defaultMessage.orDefaultValidationMessage()
                        )
                    }
                    fieldViolations + objectViolations
                } else {
                    val field = result.methodParameter.parameterName ?: "parameter"
                    result.resolvableErrors.map { e ->
                        FieldViolation(
                            field = field,
                            message = e.defaultMessage.orDefaultValidationMessage()
                        )
                    }
                }
            }

        val crossParameterViolations = ex.crossParameterValidationResults
            .map { e ->
                FieldViolation(
                    field = "request",
                    message = e.defaultMessage.orDefaultValidationMessage()
                )
            }

        val violations = (parameterViolations + crossParameterViolations)
            .sortedBy(FieldViolation::field)

        return invalidRequestResponse(violations)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception
    ): ResponseEntity<ApiErrorResponse> {
        val traceId = UUID.randomUUID().toString()

        log.error(ex) { "처리되지 않은 예외 traceId=$traceId type=${ex.javaClass.simpleName}" }

        val details = mapOf<String, Any?>(
            "traceId" to traceId
        )

        return ResponseEntity
            .status(ErrorCode.INTERNAL_SERVER_ERROR.httpStatus)
            .body(
                ApiErrorResponse.from(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    details = details
                )
            )
    }

    /**
     * Spring MVC 표준 예외(404·405·415 등)의 응답 본문을 공통 에러 응답으로 매핑.
     * 각 에러를 override하지 않아도 오류 응답 형식이 하나로 통일됨.
     */
    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorCode = ErrorCode.from(statusCode)

        log.warn { "MVC 표준 예외 code=${errorCode.code} status=${statusCode.value()} type=${ex.javaClass.simpleName}" }

        return super.handleExceptionInternal(
            ex,
            ApiErrorResponse.from(errorCode),
            headers,
            statusCode,
            request
        )
    }

    private fun invalidRequestResponse(
        violations: List<FieldViolation>
    ): ResponseEntity<Any> {
        log.warn { "요청 검증 실패 fields=${violations.map(FieldViolation::field)}" }

        val errorCode = ErrorCode.INVALID_REQUEST
        val response = ApiErrorResponse.from(
            errorCode = errorCode,
            details = mapOf("fields" to violations)
        )

        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(response)
    }

    private fun String?.orDefaultValidationMessage(): String =
        this ?: "올바르지 않은 값입니다."

}