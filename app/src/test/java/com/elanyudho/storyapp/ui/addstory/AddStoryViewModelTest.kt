package com.elanyudho.storyapp.ui.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elanyudho.core.extension.onSuccess
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.data.dispatcher.DispatcherProviderImpl
import com.elanyudho.storyapp.domain.usecase.stories.PostStoryUseCase
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
class AddStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var postStoryUseCase: PostStoryUseCase
    private lateinit var addStoryViewModel: AddStoryViewModel

    private val dispatcherProvider = DispatcherProviderImpl()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val dummyPhoto = DummyData.generateDummyMultipartFile()
    private val dummyDesc = DummyData.generateDummyRequestBody()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        addStoryViewModel = spyk(AddStoryViewModel(dispatcherProvider, postStoryUseCase))
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `PostStoryUseCase Successfully`() {
        val body = null
        val expectedState = Either.Success(body)

        coEvery {
            postStoryUseCase.run(
                PostStoryUseCase.Params(
                    dummyPhoto,
                    dummyDesc
                )
            )
        } returns expectedState

        runBlocking {
            val actualState = postStoryUseCase.run(
                PostStoryUseCase.Params(
                    dummyPhoto,
                    dummyDesc
                )
            )

            Assert.assertNotNull(actualState)
            Assert.assertEquals(expectedState, actualState)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `postStory Successfully - Result Success`() {
        val body = null
        val expectedState = Either.Success(body)

        val expectedData = AddStoryViewModel.AddStoryUiState.SuccessUpload(
            body
        )

        coEvery {
            postStoryUseCase.run(PostStoryUseCase.Params(dummyPhoto, dummyDesc))
                .onSuccess {
                    addStoryViewModel._uiState.value = AddStoryViewModel.AddStoryUiState.SuccessUpload(it)
                }
        } returns expectedState


        addStoryViewModel.postStory(dummyPhoto, dummyDesc)
        val actualState = addStoryViewModel.uiState

        coVerify(atLeast = 1) { postStoryUseCase.run(PostStoryUseCase.Params(dummyPhoto, dummyDesc))
            .onSuccess {
                addStoryViewModel._uiState.value = AddStoryViewModel.AddStoryUiState.SuccessUpload(it)
            } }

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedData, actualState.value)

    }
}