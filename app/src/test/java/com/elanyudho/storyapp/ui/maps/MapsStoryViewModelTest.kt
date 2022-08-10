package com.elanyudho.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elanyudho.core.abstraction.UseCase
import com.elanyudho.core.extension.onSuccess
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.data.dispatcher.DispatcherProviderImpl
import com.elanyudho.storyapp.domain.usecase.stories.GetStoriesLocationUseCase
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
class MapsStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getStoriesLocationUseCase: GetStoriesLocationUseCase
    private lateinit var mapsStoryViewModel: MapsStoryViewModel

    private val dispatcherProvider = DispatcherProviderImpl()

    private val testDispatcher = UnconfinedTestDispatcher()

    private val dummyStories= DummyData.generateDummyListStory()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        mapsStoryViewModel = spyk(MapsStoryViewModel(dispatcherProvider, getStoriesLocationUseCase))
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GetRegisterUseCase Successfully`() {
        val expectedState = Either.Success(
            body = dummyStories
        )

        coEvery {
            getStoriesLocationUseCase.run(UseCase.None)
        } returns expectedState

        runBlocking {
            val actualState = getStoriesLocationUseCase.run(UseCase.None)

            Assert.assertNotNull(actualState)
            Assert.assertEquals(expectedState, actualState)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun `getStoriesLocation Successfully - Result Success`() {
        val expectedState = Either.Success(
           dummyStories
        )

        val expectedData = MapsStoryViewModel.MapsStoryUiState.StoriesLoaded(dummyStories)

        coEvery {
            getStoriesLocationUseCase.run(UseCase.None)
                .onSuccess {
                    mapsStoryViewModel._uiState.value = MapsStoryViewModel.MapsStoryUiState.StoriesLoaded(it)
                }
        } returns expectedState


        mapsStoryViewModel.getStoriesLocation()
        val actualState = mapsStoryViewModel.uiState

        coVerify(atLeast = 1) { getStoriesLocationUseCase.run(UseCase.None)
            .onSuccess {
                mapsStoryViewModel._uiState.value = MapsStoryViewModel.MapsStoryUiState.StoriesLoaded(it)
            } }

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedData, actualState.value)

    }
}