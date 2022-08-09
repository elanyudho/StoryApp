package com.elanyudho.storyapp.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.elanyudho.core.abstraction.BaseViewModel
import com.elanyudho.core.dispatcher.DispatcherProvider
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.extension.onError
import com.elanyudho.core.extension.onSuccess
import com.elanyudho.storyapp.domain.model.User
import com.elanyudho.storyapp.domain.usecase.auth.GetLoginUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val loginUseCase: GetLoginUseCase
) : BaseViewModel<LoginViewModel.LoginUiState>() {

    sealed class LoginUiState {
        object Loading : LoginUiState()
        data class Success(val body: User) : LoginUiState()
        data class Failed(val failure: Failure) : LoginUiState()
    }

    fun doLogin(userName: String, password: String) {
        _uiState.value = LoginUiState.Loading
        println("LoadingDoLogin")
        viewModelScope.launch(dispatcherProvider.io) {
            println("usecaseCall")
            loginUseCase.run(GetLoginUseCase.Params(userName, password))
                .onSuccess {
                    withContext(dispatcherProvider.main) {
                        println("SuccessDoLogin")
                        _uiState.value = LoginUiState.Success(it)
                    }
                }
                .onError {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = LoginUiState.Failed(it)
                    }
                }
        }
    }
}