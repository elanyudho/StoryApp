package com.elanyudho.storyapp.domain.usecase.stories

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.domain.repository.StoryRepository
import javax.inject.Inject

class GetStoriesPagingUseCase @Inject constructor(val repo: StoryRepository) {

    fun getStoriesPaging(): LiveData<PagingData<StoryListResponse.Story>> {
        return repo.getStoriesPaging()
    }
}