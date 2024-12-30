package com.yangian.numsum.core.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.firebase.repository.FirestoreRepository
import com.yangian.numsum.core.firebase.repository.FirestoreResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class LogsUploadWorker @AssistedInject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val userPreferences: UserPreferences,
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private fun scheduleWorker(retryPolicy: Long) {
        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<LogsUploadWorker>(
            repeatInterval = retryPolicy,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
        ).setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            2,
            TimeUnit.MINUTES,
        ).setConstraints(
            workerConstraints
        ).build()

        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            "LOGS_UPLOAD_WORKER",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }


    override suspend fun doWork(): Result {
        lateinit var result: Result

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60 * 60 * 24 * 3 // 3 Days
        }
        var existingUserPreferences = userPreferences.getWorkerRetryPolicy().first()

        var updateWorkerPolicy = false

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.fetchAndActivate()
            .addOnSuccessListener {
                val workerRetryPolicy = firebaseRemoteConfig.getLong("WorkerRetryPolicy")
                if (existingUserPreferences != workerRetryPolicy) {
                    existingUserPreferences = workerRetryPolicy
                    updateWorkerPolicy = true
                }
            }

        if (updateWorkerPolicy) {
            userPreferences.setWorkerRetryPolicy(existingUserPreferences)
            scheduleWorker(existingUserPreferences)
            return Result.success()
        }

        try {

            val senderId = firebaseAuth.currentUser?.uid!!
            val receiverId = userPreferences.getReceiverId().first()

            val firestoreResult = firestoreRepository.addData(
                senderId,
                receiverId,
                context
            )

            result = when (firestoreResult) {
                is FirestoreResult.Success -> Result.success()
                is FirestoreResult.Failure -> Result.failure()
                is FirestoreResult.Retry -> Result.retry()
            }


        } catch (e: Exception) {
            result = Result.retry()
        }
        return result
    }
}