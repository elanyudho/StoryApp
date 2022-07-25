package com.elanyudho.storyapp.di

import com.elanyudho.core.dispatcher.DispatcherProvider
import com.elanyudho.storyapp.data.dispatcher.DispatcherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class CoroutinesDispatcherModule {

    @Binds
    @ActivityScoped
    abstract fun bindDispatcherProvider(dispatcherProvider: DispatcherProviderImpl): DispatcherProvider

}