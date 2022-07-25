package com.elanyudho.storyapp.ui.auth.register

import androidx.lifecycle.viewModelScope
import com.elanyudho.core.abstraction.BaseViewModel
import com.elanyudho.core.dispatcher.DispatcherProvider
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.extension.onError
import com.elanyudho.core.extension.onSuccess
import com.elanyudho.storyapp.domain.model.Register
import com.elanyudho.storyapp.domain.usecase.auth.GetRegisterUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getRegisterUseCase: GetRegisterUseCase
) : BaseViewModel<RegisterViewModel.RegisterUiState>(){

    sealed class RegisterUiState {
        object Loading : RegisterUiState()
        data class Success(val register: Register) : RegisterUiState()
        data class Failed(val failure: Failure) : RegisterUiState()
    }

    fun doRegister(userName: String, email: String, password: String) {
        _uiState.value = RegisterUiState.Loading
        viewModelScope.launch(dispatcherProvider.io) {

            getRegisterUseCase.run(GetRegisterUseCase.Params(userName, email, password))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RegisterUiState.Success(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = RegisterUiState.Failed(it)
                    }
                }
        }
    }
}