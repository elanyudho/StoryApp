package com.elanyudho.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.extension.onError
import com.elanyudho.core.vo.Either
import com.elanyudho.core.vo.RequestResults
import com.elanyudho.storyapp.data.FakeApiService
import com.elanyudho.storyapp.data.remote.mapper.LoginMapper
import com.elanyudho.storyapp.data.remote.mapper.RegisterMapper
import com.elanyudho.storyapp.data.remote.service.ApiService
import com.elanyudho.storyapp.data.remote.source.RemoteDataSource
import com.elanyudho.storyapp.domain.repository.AuthRepository
import com.elanyudho.storyapp.utils.dummydata.DummyData
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExtendWith(MockKExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class AuthRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var loginMapper: LoginMapper
    private lateinit var registerMapper: RegisterMapper

    @MockK
    private lateinit var authRepository: AuthRepository

    private val dummyEmail = "elanyudho@gmail.com"
    private val dummyPassword = "password"
    private val dummyUsername = "elanyudho"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        apiService = FakeApiService()
        loginMapper = LoginMapper()
        registerMapper = RegisterMapper()
        remoteDataSource = spyk(RemoteDataSource(apiService))
        authRepository = spyk(AuthRepositoryImpl(remoteDataSource, registerMapper, loginMapper))
    }

    @Test
    fun `register - Result Success`() = runTest {
        val expectedState = Either.Success(DummyData.generateDummyRegisterResponse())

        val actualState = authRepository.register(dummyUsername, dummyEmail, dummyPassword)

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedState, actualState)

    }

    @Test
    fun `login - Result Success`() = runTest {
        val expectedStateSuccess = Either.Success(DummyData.generateDummyLogin())

        val actualState = authRepository.login(dummyEmail, dummyPassword)

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedStateSuccess, actualState)

    }

    @Test
    fun `login - Result Failed(Invalid Input)`() = runTest {
        val throwable= Throwable(message = "[401] - [Response.error()]")
        val expectedStateError = Either.Error(failure= Failure(requestResults= RequestResults.SERVER_ERROR, throwable= throwable, code="401"))

        val actualState = authRepository.login("elanyudho.work@gmail.com", dummyPassword)
        var actualCodeError = ""
        actualState.onError { failure -> actualCodeError = failure.code  }

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedStateError.failure.code, actualCodeError )

    }

}
