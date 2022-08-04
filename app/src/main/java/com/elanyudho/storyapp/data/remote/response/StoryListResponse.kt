package com.elanyudho.storyapp.data.remote.response


import com.google.gson.annotations.SerializedName

data class StoryListResponse(
    @SerializedName("error")
    val error: Boolean = false,
    @SerializedName("listStory")
    val listStory: List<Story> = listOf(),
    @SerializedName("message")
    val message: String = ""
) {
    data class Story(
        @SerializedName("createdAt")
        val createdAt: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("lat")
        val lat: Double? = 0.0,
        @SerializedName("lon")
        val lon: Double? = 0.0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("photoUrl")
        val photoUrl: String = ""
    )
}