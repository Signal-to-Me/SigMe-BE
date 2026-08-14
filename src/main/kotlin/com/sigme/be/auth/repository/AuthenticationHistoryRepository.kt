package com.sigme.be.auth.repository

import com.sigme.be.auth.entity.AuthenticationHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AuthenticationHistoryRepository : JpaRepository<AuthenticationHistory, UUID>{
}