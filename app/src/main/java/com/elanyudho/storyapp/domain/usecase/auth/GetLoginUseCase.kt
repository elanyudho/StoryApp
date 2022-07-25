package com.elanyudho.storyapp.domain.usecase.auth

import com.elanyudho.core.abstraction.UseCase
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.domain.model.User
import com.elanyudho.storyapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetLoginUseCase @Inject constructor(private val repo: AuthRepository): UseCase<User, GetLoginUseCase.Params>(){

    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun run(params: Params): Either<Failure, User> {
        return repo.login(params.email, params.password)
    }
}