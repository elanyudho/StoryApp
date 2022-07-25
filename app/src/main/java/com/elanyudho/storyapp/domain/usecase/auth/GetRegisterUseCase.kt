package com.elanyudho.storyapp.domain.usecase.auth

import com.elanyudho.core.abstraction.UseCase
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.domain.model.Register
import com.elanyudho.storyapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetRegisterUseCase @Inject constructor(private val repo: AuthRepository): UseCase<Register, GetRegisterUseCase.Params>(){

    data class Params(
        val username: String,
        val email: String,
        val password: String
    )

    override suspend fun run(params: Params): Either<Failure, Register> {
        return repo.register(params.username, params.email, params.password)
    }
}