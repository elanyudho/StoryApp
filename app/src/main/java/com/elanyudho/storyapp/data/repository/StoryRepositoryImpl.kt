package com.elanyudho.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.elanyudho.core.exception.Failure
import com.elanyudho.core.vo.Either
import com.elanyudho.core.vo.RequestResults
import com.elanyudho.storyapp.data.remote.mapper.StoriesMapper
import com.elanyudho.storyapp.data.remote.response.StoryListResponse
import com.elanyudho.storyapp.data.remote.service.ApiService
import com.elanyudho.storyapp.data.remote.source.RemoteDataSource
import com.elanyudho.storyapp.data.remote.source.StoryPagingSource
import com.elanyudho.storyapp.domain.model.Story
import com.elanyudho.storyapp.domain.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val storiesMapper: StoriesMapper,
    private val apiSevice: ApiService
) : StoryRepository {

    override suspend fun getStories(page: String): Either<Failure, List<Story>> {
        return when (val response = remoteDataSource.getStories(page)) {
            is Either.Success -> {
                val data = storiesMapper.mapToDomain(response.body)
                if (!response.body.error) {
                    Either.Success(data)
                } else {
                    Either.Error(
                        Failure(
                            RequestResults.SERVER_ERROR,
                            Throwable(
                                response.body.message
                            )
                        )
                    )
                }
            }
            is Either.Error -> {
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun postStory(
        photoStory: MultipartBody.Part,
        desc: RequestBody
    ): Either<Failure, Nothing?> {
        return when (val response = remoteDataSource.postAddStory(photoStory, desc)) {
            is Either.Success -> {
                if (!response.body.error) {
                    Either.Success(null)
                } else {
                    Either.Error(
                        Failure(
                            RequestResults.SERVER_ERROR,
                            Throwable(response.body.message)
                        )
                    )
                }
            }
            is Either.Error -> {
                Either.Error(response.failure)
            }
        }
    }

    override suspend fun getStoriesLocation(): Either<Failure, List<Story>> {
        return when (val response = remoteDataSource.getStoriesLocation()) {
            is Either.Success -> {
                val data = storiesMapper.mapToDomain(response.body)
                if (!response.body.error) {
                    Either.Success(data)
                } else {
                    Either.Error(
                        Failure(
                            RequestResults.SERVER_ERROR,
                            Throwable(
                                response.body.message
                            )
                        )
                    )
                }
            }
            is Either.Error -> {
                Either.Error(response.failure)
            }
        }
    }

    override fun getStoriesPaging(): LiveData<PagingData<StoryListResponse.Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3,
                initialLoadSize = 9
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiSevice)
            },
        ).liveData
    }
}
