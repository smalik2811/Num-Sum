package com.yangian.numsum

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.firebase.repository.FirestoreRepository
import com.yangian.numsum.core.workmanager.LogsUploadWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NumSumApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: LogsUpdateWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .setWorkerFactory(workerFactory)
            .build()

}

class LogsUpdateWorkerFactory @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = LogsUploadWorker(
        firestoreRepository = firestoreRepository,
        firebaseAuth = firebaseAuth,
        userPreferences = userPreferences,
        context = appContext,
        workerParameters = workerParameters
    )

}