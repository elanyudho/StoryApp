package com.elanyudho.storyapp.ui.addstory

import androidx.lifecycle.viewModelScope
import com.elanyudho.core.abstraction.BaseViewModel
import com.elanyudho.core.dispatcher.DispatcherProvider
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.extension.onError
import com.elanyudho.core.extension.onSuccess
import com.elanyudho.storyapp.domain.usecase.stories.PostStoryUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddStoryViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val postStoryUseCase: PostStoryUseCase
) : BaseViewModel<AddStoryViewModel.AddStoryUiState>(){

    sealed class AddStoryUiState {
        object Loading: AddStoryUiState()
        data class SuccessUpload(val stories: Nothing?): AddStoryUiState()
        data class FailedUpload(val failure: Failure): AddStoryUiState()
    }

    fun postStory(photoStory: MultipartBody.Part, desc: RequestBody) {
        _uiState.value = AddStoryUiState.Loading

        viewModelScope.launch(dispatcherProvider.io) {
            postStoryUseCase.run(PostStoryUseCase.Params(photoStory, desc))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddStoryUiState.SuccessUpload(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = AddStoryUiState.FailedUpload(it)
                    }
                }
        }
    }
}