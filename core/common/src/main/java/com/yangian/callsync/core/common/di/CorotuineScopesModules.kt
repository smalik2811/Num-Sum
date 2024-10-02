package com.yangian.callsync.core.common.di

import com.yangian.callsync.core.common.Dispatcher
import com.yangian.callsync.core.common.NSDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
internal object CoroutineScopesModule {
    @Provides
    @Singleton
    @ApplicationContext
    fun providesCoroutineScope(
        @Dispatcher(NSDispatchers.Default) dispatcher: CoroutineDispatcher,

        ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}