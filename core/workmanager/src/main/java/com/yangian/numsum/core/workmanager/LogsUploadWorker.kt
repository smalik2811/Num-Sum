package com.yangian.numsum.core.workmanager

import android.content.Context
import androidx.annotation.Keep
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.firebase.repository.FirestoreRepository
import com.yangian.numsum.core.firebase.repository.FirestoreResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class LogsUploadWorker @AssistedInject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences,
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        lateinit var result: Result
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