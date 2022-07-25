package com.elanyudho.storyapp.data.remote.mapper

import com.elanyudho.core.abstraction.BaseMapper
import com.elanyudho.storyapp.data.remote.response.LoginResponse
import com.elanyudho.storyapp.domain.model.User

class LoginMapper : BaseMapper<LoginResponse, User> {
    override fun mapToDomain(raw: LoginResponse): User {
        return User(
            userId = raw.loginResult.userId,
            username = raw.loginResult.name,
            token = raw.loginResult.token
        )
    }

    override fun mapToRaw(domain: User): LoginResponse {
        return LoginResponse()
    }
}