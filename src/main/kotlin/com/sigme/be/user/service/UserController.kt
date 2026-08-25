package com.sigme.be.user.service

import com.sigme.be.auth.dto.request.EmailLoginRequest
import com.sigme.be.user.dto.request.EmailSignUpRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/signup/email")
    fun signup(@Valid @RequestBody request: EmailSignUpRequest)
    = ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(request))

    @PostMapping("/login/email")
    fun login(@Valid @RequestBody request: EmailLoginRequest)
    = userService.login(request)
}