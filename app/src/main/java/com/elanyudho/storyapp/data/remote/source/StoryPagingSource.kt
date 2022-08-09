package com.elanyudho.storyapp.data.remote.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.data.remote.service.ApiService
import javax.inject.Inject

class StoryPagingSource @Inject constructor(private val api: ApiService) : PagingSource<Int, StoryListResponse.Story>() {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryListResponse.Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = api.getStoriesPaging(position)
            Log.d("RESPONSEDATA", responseData.listStory.toString())
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryListResponse.Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}