package com.elanyudho.storyapp.di

import com.elanyudho.storyapp.data.repository.AuthRepositoryImpl
import com.elanyudho.storyapp.data.repository.StoryRepositoryImpl
import com.elanyudho.storyapp.domain.repository.AuthRepository
import com.elanyudho.storyapp.domain.repository.StoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    @ActivityScoped
    abstract fun bindAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @ActivityScoped
    abstract fun bindStoriesRepository(repositoryImpl: StoryRepositoryImpl): StoryRepository

}