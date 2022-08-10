package com.elanyudho.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.elanyudho.core.vo.Either
import com.elanyudho.storyapp.data.FakeApiService
import com.elanyudho.storyapp.data.remote.mapper.StoriesMapper
import com.elanyudho.storyapp.data.remote.service.ApiService
import com.elanyudho.storyapp.data.remote.source.RemoteDataSource
import com.elanyudho.storyapp.data.remote.source.StoryPagingSource
import com.elanyudho.storyapp.domain.repository.StoryRepository
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
class StoryRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var pagingSource: StoryPagingSource
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var storiesMapper: StoriesMapper

    @MockK
    private lateinit var storyRepository: StoryRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        apiService = FakeApiService()
        storiesMapper = StoriesMapper()
        remoteDataSource = spyk(RemoteDataSource(apiService))
        pagingSource = spyk(StoryPagingSource(apiService))
        storyRepository = spyk(StoryRepositoryImpl(remoteDataSource, storiesMapper, apiService))

    }

    @Test
    fun `getStories - Result Success`() = runTest{
        val expectedState = Either.Success(DummyData.generateDummyListStory())

        val actualState = storyRepository.getStories("1")

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedState, actualState)

    }

    @Test
    fun `getStoriesLocation - Result Success`() = runTest{
        val expectedState = Either.Success(DummyData.generateDummyListStory())

        val actualState = storyRepository.getStoriesLocation()

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedState, actualState)

    }

    @Test
    fun `postStories - Result Success`() = runTest{
        val dummyPhoto = DummyData.generateDummyMultipartFile()
        val dummyDesc = DummyData.generateDummyRequestBody()

        val expectedState = Either.Success(null)

        val actualState = storyRepository.postStory(dummyPhoto, dummyDesc)

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedState, actualState)

    }

    @Test
    fun `getStoriesPaging - Result Success`()  {
        val expectedResult = Pager(
            config = PagingConfig(
                pageSize = 3,
                initialLoadSize = 9
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            },
        ).liveData


        val actualState = storyRepository.getStoriesPaging()

        Assert.assertNotNull(actualState)
        Assert.assertEquals(expectedResult.value, actualState.value)

    }

}