package com.elanyudho.storyapp.domain.usecase.stories

import com.elanyudho.core.abstraction.UseCase
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.domain.model.Story
import com.elanyudho.storyapp.domain.repository.StoryRepository
import javax.inject.Inject

class GetStoriesLocationUseCase @Inject constructor(private val repo: StoryRepository): UseCase<List<Story>, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, List<Story>> {
        return repo.getStoriesLocation()
    }
}