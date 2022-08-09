package com.elanyudho.storyapp.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elanyudho.core.extension.onSuccess
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.data.dispatcher.DispatcherProviderImpl
import com.elanyudho.storyapp.domain.model.Register
import com.elanyudho.storyapp.domain.usecase.auth.GetRegisterUseCase
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
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var registerUseCase: GetRegisterUseCase
    private lateinit var registerViewModel: RegisterViewModel

    private val dispatcherProvider = DispatcherProviderImpl()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val dummyRegister = DummyData.generateDummyDefaultResponse()
    private val dummyEmail = "elanyudho@mail.com"
    private val dummyPassword = "password"
    private val dummyUsername = "elanyudho"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        registerViewModel = spyk(RegisterViewModel(dispatcherProvider, registerUseCase))
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Testing getRegisterUseCase`() {
        val expectedState = Either.Success(
            Register(
                errorStatus = dummyRegister.error,
                message = dummyRegister.message
            )
        )

        coEvery {
            registerUseCase.run(
                GetRegisterUseCase.Params(
                    dummyUsername,
                    dummyEmail,
                    dummyPassword
                )
            )
        } returns expectedState

        runBlocking {
            val actualState = registerUseCase.run(
                GetRegisterUseCase.Params(
                    dummyUsername,
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
    fun `Register Successfully - Result Success`() {
        val expectedState = Either.Success(
            Register(
                errorStatus = dummyRegister.error,
                message = dummyRegister.message
            )
        )

        val expectedData = RegisterViewModel.RegisterUiState.Success(
            Register(
                errorStatus = dummyRegister.error,
                message = dummyRegister.message
            )
        )

        coEvery {
            registerUseCase.run(GetRegisterUseCase.Params(dummyUsername, dummyEmail, dummyPassword))
                .onSuccess {
                    registerViewModel._uiState.value = RegisterViewModel.RegisterUiState.Success(it)
                }
        } returns expectedState


        registerViewModel.doRegister(dummyUsername, dummyEmail,dummyPassword)
        val actualState = registerViewModel.uiState

        coVerify(atLeast = 1) { registerUseCase.run(GetRegisterUseCase.Params(dummyUsername, dummyEmail, dummyPassword))
            .onSuccess {
                registerViewModel._uiState.value = RegisterViewModel.RegisterUiState.Success(it)
            } }

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedData, actualState.value)

    }
}