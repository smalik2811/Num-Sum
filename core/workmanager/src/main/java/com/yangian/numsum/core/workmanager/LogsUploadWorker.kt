package com.yangian.numsum.core.workmanager

import android.content.Context
import android.provider.CallLog
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yangian.numsum.core.data.repository.CallResourceRepository
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.model.CallResource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@HiltWorker
class LogsUploadWorker @AssistedInject constructor(
    @Assisted private val firestore: FirebaseFirestore,
    @Assisted private val firebaseAuth: FirebaseAuth,
    @Assisted private val userPreferences: UserPreferences,
    @Assisted private val callResourcesRepository: CallResourceRepository,
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private var lastCallId: Flow<Long> = userPreferences.getLastCallId()
    private suspend fun getLastCallId(): Long {
        return lastCallId.first()
    }

    override suspend fun doWork(): Result {

        return try {
            // 1. Fetch and store logs
            storeLogsInLocalDatabase(context)

            // 2. Get call list
            val callList = callResourcesRepository.getCalls().first()

            // 3. Retrieve receiver ID and document reference
            val currentUser = firebaseAuth.currentUser
            val receiverId = userPreferences.getReceiverId().first()
            val documentRef = firestore.collection("logs").document(receiverId)

            // 4. Update the Firestore document
            if (currentUser != null && callList.isNotEmpty()) {
                // 4.1. Get document data
                val documentSnapshot = documentRef.get().await()

                // 4.2. Check for existing logs and sender ID
                if (documentSnapshot.exists()) {
                    val senderId = documentSnapshot.getString("sender")
                    val ready = documentSnapshot.get("ready") as Boolean

                    if (senderId == currentUser.uid && !ready) {
                        // 4.3. Update the document with logs
                        documentRef.update(
                            mapOf(
                                "array" to callList,
                                "ready" to true
                            )
                        )
                    }
                }
            }

            // 5. Delete local logs (if upload successful)
            withContext(Dispatchers.IO) {
                callResourcesRepository.deleteCalls()
                println("Worker: Finished the task.")
            }

            println("Worker: Finished Properly. Time: ${System.currentTimeMillis()}")

            Result.success()

        } catch (e: Exception) {
            Log.e("LogsUpdateWorker", e.message.toString())
            Result.retry()
        }
    }

    // Helper function for awaiting Firestore operations
    private suspend fun <T> Task<T>.await(): T {
        return suspendCoroutine { continuation ->
            addOnSuccessListener { continuation.resume(it) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }


    private suspend fun storeLogsInLocalDatabase(
        context: Context
    ) {

        val idColumn = CallLog.Calls._ID
        val nameColumn = CallLog.Calls.CACHED_NAME
        val numberColumn = CallLog.Calls.NUMBER
        val dateColumn = CallLog.Calls.DATE
        val durationColumn = CallLog.Calls.DURATION
        val typeColumn = CallLog.Calls.TYPE

        val projection = arrayOf(
            idColumn,
            nameColumn,
            numberColumn,
            dateColumn,
            durationColumn,
            typeColumn,
        )

        val listOfCallResource = mutableListOf<CallResource>()
        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection,
            "_id > ?",
            arrayOf(getLastCallId().toString()),
            "_id ASC"
        )

        val idColIdx = cursor!!.getColumnIndex(idColumn)
        val nameColIdx = cursor.getColumnIndex(nameColumn)
        val numberColIdx = cursor.getColumnIndex(numberColumn)
        val dateColIdx = cursor.getColumnIndex(dateColumn)
        val durationColIdx = cursor.getColumnIndex(durationColumn)
        val typeColIdx = cursor.getColumnIndex(typeColumn)

        while (cursor.moveToNext()) {
            val callResource = CallResource(
                id = cursor.getLong(idColIdx),
                name = cursor.getString(nameColIdx),
                number = cursor.getString(numberColIdx),
                timestamp = cursor.getLong(dateColIdx),
                duration = cursor.getLong(durationColIdx),
                type = cursor.getInt(typeColIdx),
            )
            listOfCallResource.add(callResource)
        }

        cursor.close()

        if (listOfCallResource.isNotEmpty()) {
            val ioScope = CoroutineScope(Dispatchers.IO)
            callResourcesRepository.addCalls(listOfCallResource.toList())
            userPreferences.updateLastCallId(listOfCallResource.last().id)

        }
    }
}