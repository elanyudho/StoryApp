package com.elanyudho.storyapp.di

import com.elanyudho.storyapp.domain.repository.AuthRepository
import com.elanyudho.storyapp.domain.repository.StoryRepository
import com.elanyudho.storyapp.domain.usecase.auth.GetLoginUseCase
import com.elanyudho.storyapp.domain.usecase.auth.GetRegisterUseCase
import com.elanyudho.storyapp.domain.usecase.stories.GetStoriesLocationUseCase
import com.elanyudho.storyapp.domain.usecase.stories.GetStoriesPagingUseCase
import com.elanyudho.storyapp.domain.usecase.stories.GetStoriesUseCase
import com.elanyudho.storyapp.domain.usecase.stories.PostStoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object UseCaseModule {

    @Provides
    @ActivityScoped
    fun provideRegisterUseCase(repository: AuthRepository) = GetRegisterUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideLoginUseCase(repository: AuthRepository) = GetLoginUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideStoriesUseCase(repository: StoryRepository) = GetStoriesUseCase(repository)

    @Provides
    @ActivityScoped
    fun providePostStoryUseCase(repository: StoryRepository) = PostStoryUseCase(repository)

    @Provides
    @ActivityScoped
    fun providePostStoryLocationUseCase(repository: StoryRepository) = GetStoriesLocationUseCase(repository)

    @Provides
    @ActivityScoped
    fun providePagingStoriesUseCase(repository: StoryRepository) = GetStoriesPagingUseCase(repository)
}