package com.elanyudho.storyapp.utils.dummydata

import com.elanyudho.storyapp.data.remote.response.DefaultResponse
import com.elanyudho.storyapp.data.remote.response.LoginResponse
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.domain.model.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DummyData {
    fun generateDummyStoriesResponse(): StoryListResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<StoryListResponse.Story>()

        for (i in 0 until 10) {
            val story = StoryListResponse.Story(
                id = "story-vRY0fkPkBX_cUVqx",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1659705781560_Tz8VdM_k.jpg",
                createdAt = "2022-08-05T13:23:01.562Z",
                name = "Elan",
                description = "Avenger",
                lon = -15.005,
                lat = -11.232
            )

            listStory.add(story)
        }

        return StoryListResponse(error, listStory, message)
    }

    fun generateDummyListStory(): List<Story> {
        val items = arrayListOf<Story>()

        for (i in 0 until 10) {
            val story = Story(
                id = "story-vRY0fkPkBX_cUVqx",
                imageUrl = "https://story-api.dicoding.dev/images/stories/photos-1659705781560_Tz8VdM_k.jpg",
                createdAt = "2022-08-05T13:23:01.562Z",
                username = "Elan",
                description = "Avenger",
                long = -15.005,
                lat = -11.232
            )

            items.add(story)
        }

        return items
    }


    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResponse.LoginResult(
            userId = "user-mcrwFgIbC4Wgqz7-",
            name = "elanyudho",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLW1jcndGZ0liQzRXZ3F6Ny0iLCJpYXQiOjE2NTk3ODg0NDl9.l4L1knlQemzJzgi8NBcimlU0v9M_C8lej218USCRKdQ"
        )

        return LoginResponse(
            loginResult = loginResult,
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegisterResponse(): DefaultResponse {
        return DefaultResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }

    fun generateDummyDefaultResponse(): DefaultResponse {
        return DefaultResponse(
            error = false,
            message = "success"
        )
    }
}