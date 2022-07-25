package com.elanyudho.storyapp.domain.usecase.stories

import com.elanyudho.core.abstraction.UseCase
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.domain.model.Story
import com.elanyudho.storyapp.domain.repository.StoryRepository
import javax.inject.Inject

class GetStoriesUseCase @Inject constructor(private val repo: StoryRepository): UseCase<List<Story>, String>(){

    override suspend fun run(params: String): Either<Failure, List<Story>> {
        return repo.getStories(params)
    }
}