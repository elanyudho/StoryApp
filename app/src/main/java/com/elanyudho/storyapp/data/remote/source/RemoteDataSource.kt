package com.elanyudho.storyapp.data.remote.source

import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.data.remote.response.DefaultResponse
import com.elanyudho.storyapp.data.remote.response.LoginResponse
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.data.remote.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RemoteDataSource
@Inject constructor(private val api: ApiService) : RemoteSafeRequest() {

    suspend fun postRegister(username: String, email: String, password: String): Either<Failure, DefaultResponse> =
        request {
            api.register(username, email, password)
        }

    suspend fun postLogin(email: String, password: String): Either<Failure, LoginResponse> =
        request {
            api.login(email, password)
        }

    suspend fun getStories(page: String): Either<Failure, StoryListResponse> =
        request {
            api.getStories(page)
        }

    suspend fun getStoriesLocation(): Either<Failure, StoryListResponse> =
        request {
            api.getStoriesLocation()
        }

    suspend fun postAddStory(photoStory: MultipartBody.Part, desc: RequestBody): Either<Failure, DefaultResponse> = request {
        api.postStory(photoStory,desc)
    }
}