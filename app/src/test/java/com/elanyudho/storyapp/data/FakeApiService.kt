package com.elanyudho.storyapp.data

import com.elanyudho.storyapp.data.remote.response.DefaultResponse
import com.elanyudho.storyapp.data.remote.response.LoginResponse
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.data.remote.service.ApiService
import com.elanyudho.storyapp.utils.dummydata.DummyData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeApiService : ApiService {

    private val dummyRegisterResponse = DummyData.generateDummyDefaultResponse()
    private val dummyLoginResponse = DummyData.generateDummyLoginResponse()
    private val dummyLoginAuth = DummyData.generateAuthLogin()
    private val dummyStoriesResponse = DummyData.generateDummyStoriesResponse()
    private val dummyDefaultResponse = DummyData.generateDummyDefaultResponse()

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Response<DefaultResponse> {
        return Response.success(dummyRegisterResponse)
    }

    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        return if (email == dummyLoginAuth.email && password == dummyLoginAuth.password) {
            Response.success(dummyLoginResponse)
        } else {
            val errorResponse = "{\n" + "  \"type\": \"error\",\n" + "  \"message\": \"Invalid Data.\"\n" + "}"
            val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
            Response.error(401, errorResponseBody)
        }
    }

    override suspend fun getStories(page: String): Response<StoryListResponse> {
        return Response.success(dummyStoriesResponse)
    }

    override suspend fun getStoriesPaging(page: Int): StoryListResponse {
        return dummyStoriesResponse
    }

    override suspend fun getStoriesLocation(): Response<StoryListResponse> {
        return Response.success(dummyStoriesResponse)
    }

    override suspend fun postStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): Response<DefaultResponse> {
        return Response.success(dummyDefaultResponse)
    }
}