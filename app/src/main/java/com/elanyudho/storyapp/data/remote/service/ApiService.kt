package com.elanyudho.storyapp.data.remote.service

import com.elanyudho.storyapp.data.remote.response.DefaultResponse
import com.elanyudho.storyapp.data.remote.response.LoginResponse
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<DefaultResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: String
    ): Response<StoryListResponse>

    @GET("stories")
    suspend fun getStoriesPaging(
        @Query("page") page: Int
    ): StoryListResponse

    @GET("stories?location=1")
    suspend fun getStoriesLocation(): Response<StoryListResponse>

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Response<DefaultResponse>
}
