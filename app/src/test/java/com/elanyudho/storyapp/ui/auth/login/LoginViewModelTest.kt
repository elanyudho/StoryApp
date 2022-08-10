package com.elanyudho.storyapp.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elanyudho.core.extension.onSuccess
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.data.dispatcher.DispatcherProviderImpl
import com.elanyudho.storyapp.domain.model.User
import com.elanyudho.storyapp.domain.usecase.auth.GetLoginUseCase
import com.elanyudho.storyapp.utils.dummydata.DummyData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var loginUseCase: GetLoginUseCase
    private lateinit var loginViewModel: LoginViewModel

    private val dispatcherProvider = DispatcherProviderImpl()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val dummyLogin = DummyData.generateDummyLoginResponse()
    private val dummyEmail = "elanyudho@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        loginViewModel = spyk(LoginViewModel(dispatcherProvider, loginUseCase))
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `LoginUseCase Successfully`() {
        val expectedState = Either.Success(
            User(
                dummyLogin.loginResult.userId,
                dummyLogin.loginResult.name,
                dummyLogin.loginResult.token
            )
        )

        coEvery {
            loginUseCase.run(
                GetLoginUseCase.Params(
                    dummyEmail,
                    dummyPassword
                )
            )
        } returns expectedState

        runBlocking {
            val actualState = loginUseCase.run(
                GetLoginUseCase.Params(
                    dummyEmail,
                    dummyPassword
                )
            )

            Assert.assertNotNull(actualState)
            Assert.assertEquals(expectedState, actualState)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `doLogin Successfully - Result Success`() {
        val expectedState = Either.Success(
            User(
                dummyLogin.loginResult.userId,
                dummyLogin.loginResult.name,
                dummyLogin.loginResult.token
            )
        )

        val expectedData = LoginViewModel.LoginUiState.Success(
            User(
                dummyLogin.loginResult.userId,
                dummyLogin.loginResult.name,
                dummyLogin.loginResult.token
            )
        )

        coEvery {
            loginUseCase.run(GetLoginUseCase.Params(dummyEmail, dummyPassword))
                .onSuccess {
                loginViewModel._uiState.value = LoginViewModel.LoginUiState.Success(it)
            }
        } returns expectedState


        loginViewModel.doLogin(dummyEmail,dummyPassword)
        val actualState = loginViewModel.uiState

        coVerify(atLeast = 1) { loginUseCase.run(GetLoginUseCase.Params(dummyEmail, dummyPassword))
            .onSuccess {
                loginViewModel._uiState.value = LoginViewModel.LoginUiState.Success(it)
            } }

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedData, actualState.value)

    }

}
