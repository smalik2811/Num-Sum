package com.yangian.numsum.core.firebase.di

import com.google.firebase.firestore.FirebaseFirestore
import com.yangian.numsum.core.data.repository.CallResourceRepository
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.firebase.repository.DefaultFirestoreRepository
import com.yangian.numsum.core.firebase.repository.FirestoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseFirestoreRepository {
    @Provides
    fun providesFirestoreRepository(
        firestore: FirebaseFirestore,
        userPreferences: UserPreferences,
        callResourcesRepository: CallResourceRepository
    ): FirestoreRepository {
        return DefaultFirestoreRepository(firestore, userPreferences, callResourcesRepository)
    }
}