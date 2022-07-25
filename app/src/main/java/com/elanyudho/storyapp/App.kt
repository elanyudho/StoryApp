package com.elanyudho.storyapp

import android.app.Application
import androidx.lifecycle.LiveData
import com.elanyudho.storyapp.data.network.NetworkConnection
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        var networkConnection : LiveData<Boolean>? = null
    }

    override fun onCreate() {
        super.onCreate()

        networkConnection = NetworkConnection(this)
    }
}