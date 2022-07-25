package com.elanyudho.storyapp.ui.main

import androidx.lifecycle.viewModelScope
import com.elanyudho.core.abstraction.BaseViewModel
import com.elanyudho.core.dispatcher.DispatcherProvider
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.extension.onError
import com.elanyudho.core.extension.onSuccess
import com.elanyudho.storyapp.domain.model.Story
import com.elanyudho.storyapp.domain.usecase.stories.GetStoriesUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getStoriesUseCase: GetStoriesUseCase
) : BaseViewModel<MainViewModel.MainUiState>(){

    sealed class MainUiState {
        object InitialLoading: MainUiState()
        object PagingLoading: MainUiState()
        data class StoriesLoaded(val stories: List<Story>): MainUiState()
        data class FailedLoadStories(val failure: Failure): MainUiState()
    }

    fun getStories(page: Long){
        _uiState.value = if (page == 1L) {
            MainUiState.InitialLoading
        }else{
            MainUiState.PagingLoading
        }

        viewModelScope.launch(dispatcherProvider.io) {
            getStoriesUseCase.run(page.toString())
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = MainUiState.StoriesLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = MainUiState.FailedLoadStories(it)
                    }
                }
        }
    }
}