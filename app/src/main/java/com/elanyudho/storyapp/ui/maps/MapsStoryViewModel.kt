package com.elanyudho.storyapp.ui.maps

import androidx.lifecycle.viewModelScope
import com.elanyudho.core.abstraction.BaseViewModel
import com.elanyudho.core.abstraction.UseCase
import com.elanyudho.core.dispatcher.DispatcherProvider
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.extension.onError
import com.elanyudho.core.extension.onSuccess
import com.elanyudho.storyapp.domain.model.Story
import com.elanyudho.storyapp.domain.usecase.stories.GetStoriesLocationUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapsStoryViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getStoriesLocationUseCase: GetStoriesLocationUseCase
) : BaseViewModel<MapsStoryViewModel.MapsStoryUiState>() {

    sealed class MapsStoryUiState {
        data class StoriesLoaded(val stories: List<Story>): MapsStoryUiState()
        data class FailedLoadStories(val failure: Failure): MapsStoryUiState()
        data class Loading(val isLoading: Boolean): MapsStoryUiState()
    }

    fun getStoriesLocation() {
        _uiState.value = MapsStoryUiState.Loading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            getStoriesLocationUseCase.run(UseCase.None)
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = MapsStoryUiState.Loading(false)
                        _uiState.value = MapsStoryUiState.StoriesLoaded(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = MapsStoryUiState.Loading(false)
                        _uiState.value = MapsStoryUiState.FailedLoadStories(it)
                    }
                }
        }

    }
}