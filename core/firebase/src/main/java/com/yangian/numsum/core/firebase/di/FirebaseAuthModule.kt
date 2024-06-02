package com.yangian.numsum.core.firebase.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseAuthModule {

    @Provides
    fun providesFirebaseAuthentication(
        firebase: Firebase
    ): FirebaseAuth {
        val auth = firebase.auth
//        auth.useEmulator("10.0.0.2", 4000)
        return auth
    }
}