package com.elanyudho.storyapp.domain.model

data class User(
    val userId: String? = "",
    val username: String = "",
    var token: String = "",
)