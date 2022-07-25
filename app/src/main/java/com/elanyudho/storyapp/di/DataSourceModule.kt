package com.elanyudho.storyapp.di

import com.elanyudho.storyapp.data.remote.service.ApiService
import com.elanyudho.storyapp.data.remote.source.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object DataSourceModule {
    @Provides
    @ActivityScoped
    fun provideRemoteDataSource(apiService: ApiService) = RemoteDataSource(apiService)
}