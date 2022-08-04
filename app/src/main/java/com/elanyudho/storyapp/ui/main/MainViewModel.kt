package com.elanyudho.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.elanyudho.core.abstraction.BaseViewModel
import com.elanyudho.core.exception.Failure
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.domain.model.Story
import com.elanyudho.storyapp.domain.usecase.stories.GetStoriesPagingUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getStoriesPagingUseCase: GetStoriesPagingUseCase
) : BaseViewModel<MainViewModel.MainUiState>(){

    sealed class MainUiState {
        object InitialLoading: MainUiState()
        object PagingLoading: MainUiState()
        data class StoriesLoaded(val stories: List<Story>): MainUiState()
        data class FailedLoadStories(val failure: Failure): MainUiState()
    }

    fun getStoriesPaging() : LiveData<PagingData<StoryListResponse.Story>> =
        getStoriesPagingUseCase.getStoriesPaging().cachedIn(viewModelScope)
}
