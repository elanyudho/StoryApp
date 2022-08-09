package com.elanyudho.storyapp.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.elanyudho.storyapp.data.remote.response.StoryListResponse

class PagedTestDataSource : PagingSource<Int, LiveData<List<StoryListResponse.Story>>>() {

    companion object {
        fun snapshot(items: List<StoryListResponse.Story>): PagingData<StoryListResponse.Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryListResponse.Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryListResponse.Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}