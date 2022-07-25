package com.elanyudho.storyapp.data.remote.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("error")
    val error: Boolean = false,
    @SerializedName("loginResult")
    val loginResult: LoginResult = LoginResult(),
    @SerializedName("message")
    val message: String = ""
) {
    data class LoginResult(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("token")
        val token: String = "",
        @SerializedName("userId")
        val userId: String = ""
    )
}