package com.elanyudho.storyapp.data.repository

import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.core.vo.RequestResults
import com.elanyudho.storyapp.data.remote.mapper.LoginMapper
import com.elanyudho.storyapp.data.remote.mapper.RegisterMapper
import com.elanyudho.storyapp.data.remote.source.RemoteDataSource
import com.elanyudho.storyapp.domain.model.Register
import com.elanyudho.storyapp.domain.model.User
import com.elanyudho.storyapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val registerMapper: RegisterMapper,
    private val loginMapper: LoginMapper
) : AuthRepository {
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Either<Failure, Register> {
        return when(val response = remoteDataSource.postRegister(username, email, password)) {
            is Either.Success -> {
                val data = registerMapper.mapToDomain(response.body)
                if (!response.body.error) {
                    Either.Success(data)
                } else {
                    Either.Error(
                        Failure(
                            RequestResults.SERVER_ERROR,
                            Throwable(response.body.message
                            )
                        )
                    )
                }
            }
            is Either.Error -> {
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun login(email: String, password: String): Either<Failure, User> {
        return when(val response = remoteDataSource.postLogin(email, password)) {
            is Either.Success -> {
                val data = loginMapper.mapToDomain(response.body)
                if (!response.body.error) {
                    Either.Success(data)
                } else {
                    Either.Error(
                        Failure(
                            RequestResults.SERVER_ERROR,
                            Throwable(response.body.message
                            )
                        )
                    )
                }
            }
            is Either.Error -> {
                Either.Error(response.failure)
            }
        }
    }
}