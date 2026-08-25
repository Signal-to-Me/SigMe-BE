package com.sigme.be.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AuthenticationConfig {
    @Bean
    fun authenticationProvider(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ): DaoAuthenticationProvider =
        DaoAuthenticationProvider(userDetailsService).also {
            it.setPasswordEncoder(passwordEncoder)
        }

    @Bean
    fun authenticationManager(
        authenticationProvider: DaoAuthenticationProvider,
    ): AuthenticationManager = ProviderManager(listOf(authenticationProvider))
}