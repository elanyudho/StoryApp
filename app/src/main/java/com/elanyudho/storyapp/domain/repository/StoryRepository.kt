package com.elanyudho.storyapp.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.domain.model.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepository {

    suspend fun getStories(page: String): Either<Failure, List<Story>>

    suspend fun postStory(photoStory: MultipartBody.Part, desc: RequestBody): Either<Failure, Nothing?>

    suspend fun getStoriesLocation (): Either<Failure, List<Story>>

    fun getStoriesPaging() : LiveData<PagingData<StoryListResponse.Story>>
}