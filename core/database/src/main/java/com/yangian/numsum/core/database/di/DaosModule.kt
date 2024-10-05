package com.yangian.numsum.core.database.di

import com.yangian.numsum.core.database.NumSumCallDatabase
import com.yangian.numsum.core.database.dao.CallResourcesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    fun providesCallResourcesDao(
        numSumCallDatabase: NumSumCallDatabase
    ): CallResourcesDao = numSumCallDatabase.callResourceDao()
}