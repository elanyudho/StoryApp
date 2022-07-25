package com.elanyudho.storyapp.data.remote.response


import com.google.gson.annotations.SerializedName

data class DefaultResponse(
    @SerializedName("error")
    val error: Boolean = false,
    @SerializedName("message")
    val message: String = ""
)