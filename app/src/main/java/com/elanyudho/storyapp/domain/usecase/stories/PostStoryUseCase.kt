package com.elanyudho.storyapp.domain.usecase.stories

import com.elanyudho.core.abstraction.UseCase
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.domain.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PostStoryUseCase @Inject constructor(private val repo: StoryRepository): UseCase<Nothing?, PostStoryUseCase.Params>() {

    data class Params (
        val photoStory: MultipartBody.Part,
        val desc: RequestBody
    )

    override suspend fun run(params: PostStoryUseCase.Params): Either<Failure, Nothing?> {
        return repo.postStory(params.photoStory, params.desc)
    }

}