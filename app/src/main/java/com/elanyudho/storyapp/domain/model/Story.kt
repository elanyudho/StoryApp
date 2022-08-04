package com.elanyudho.storyapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String?,
    val imageUrl: String?,
    val username: String?,
    val description: String?,
    val createdAt: String?,
    val long: Double?,
    val lat: Double?
) : Parcelable