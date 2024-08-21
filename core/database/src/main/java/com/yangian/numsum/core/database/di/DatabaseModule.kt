package com.yangian.numsum.core.database.di

import android.content.Context
import androidx.room.Room
import com.yangian.numsum.core.constant.Constant.CALL_DATABASE_NAME
import com.yangian.numsum.core.database.NumSumCallDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesCallDatabase(
        @ApplicationContext context: Context,
    ) : NumSumCallDatabase = Room.databaseBuilder(
        context = context,
        klass = NumSumCallDatabase::class.java,
        name = CALL_DATABASE_NAME
    ).build()
}