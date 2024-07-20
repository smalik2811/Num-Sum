package com.yangian.numsum.core.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await

@HiltWorker
class UploadWorkerScheduler(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {

        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val logsUploadWorkerRequest = OneTimeWorkRequestBuilder<LogsUploadWorker>()
            .setConstraints(workerConstraints)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        try {
            workManager.enqueueUniqueWork(
                "UPLOAD_CALL_LOGS",
                ExistingWorkPolicy.REPLACE,
                logsUploadWorkerRequest
            ).await()

            return Result.success()

        } catch (e: Exception) {
            return Result.failure()
        }
    }
}