package com.elanyudho.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.utils.PagedTestDataSource
import com.elanyudho.storyapp.utils.dummydata.DummyData
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mainViewModel: MainViewModel

    @Test
    fun `getStoriesPaging Successfully`()  {
        val observer = Observer<PagingData<StoryListResponse.Story>> {}
        try {
            val dummyStories = DummyData.generateDummyStoriesResponse()
            val data = PagedTestDataSource.snapshot(dummyStories.listStory)
            val stories = MutableLiveData<PagingData<StoryListResponse.Story>>()
            stories.value = data

            `when`(mainViewModel.getStoriesPaging()).thenReturn(stories)
            val actualStories = mainViewModel.getStoriesPaging().observeForever(observer)

            Mockito.verify(mainViewModel).getStoriesPaging()
            Assert.assertNotNull(actualStories)
        } finally {
            mainViewModel.getStoriesPaging().removeObserver(observer)
        }

    }
}