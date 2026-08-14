package com.sigme.be.auth.controller

import com.sigme.be.auth.dto.request.EmailLoginRequest
import com.sigme.be.auth.dto.response.EmailLoginResponse
import com.sigme.be.auth.service.EmailAuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class EmailLoginController (
    private val loginService: EmailAuthService
){
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: EmailLoginRequest): ResponseEntity<EmailLoginResponse> {
        val response = loginService.login(request)
        return ResponseEntity.ok(response)
    }
}