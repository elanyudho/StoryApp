package com.elanyudho.storyapp.di

import com.elanyudho.storyapp.data.remote.mapper.LoginMapper
import com.elanyudho.storyapp.data.remote.mapper.RegisterMapper
import com.elanyudho.storyapp.data.remote.mapper.StoriesMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object MapperModule {

    @Provides
    @ActivityScoped
    fun provideRegisterMapper() = RegisterMapper()

    @Provides
    @ActivityScoped
    fun provideLoginMapper() = LoginMapper()

    @Provides
    @ActivityScoped
    fun provideStoriesMapper() = StoriesMapper()
}