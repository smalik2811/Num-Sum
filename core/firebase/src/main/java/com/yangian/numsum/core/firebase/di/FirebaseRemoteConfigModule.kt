package com.yangian.numsum.core.firebase.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseRemoteConfigModule {

    @Provides
    fun provideFirebaseRemoteConfig(
        firebase: Firebase
    ): FirebaseRemoteConfig {
        val remoteConfig = firebase.remoteConfig
        return remoteConfig
    }
}