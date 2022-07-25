package com.elanyudho.storyapp.domain.repository

import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.domain.model.Register
import com.elanyudho.storyapp.domain.model.User

interface AuthRepository {

    suspend fun register(username: String, email: String, password: String): Either<Failure, Register>

    suspend fun login(email: String, password: String): Either<Failure, User>
}