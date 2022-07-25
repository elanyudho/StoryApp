package com.elanyudho.storyapp.domain.repository

import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.domain.model.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepository {

    suspend fun getStories(page: String): Either<Failure, List<Story>>

    suspend fun postStory(photoStory: MultipartBody.Part, desc: RequestBody): Either<Failure, Nothing?>


}