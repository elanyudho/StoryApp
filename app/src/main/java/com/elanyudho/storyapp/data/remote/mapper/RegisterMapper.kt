package com.elanyudho.storyapp.data.remote.mapper

import com.elanyudho.core.abstraction.BaseMapper
import com.elanyudho.storyapp.data.remote.response.DefaultResponse
import com.elanyudho.storyapp.domain.model.Register

class RegisterMapper : BaseMapper<DefaultResponse, Register> {
    override fun mapToDomain(raw: DefaultResponse): Register {
        return Register(
            errorStatus = raw.error,
            message = raw.message
        )
    }

    override fun mapToRaw(domain: Register): DefaultResponse {
        return DefaultResponse()
    }
}