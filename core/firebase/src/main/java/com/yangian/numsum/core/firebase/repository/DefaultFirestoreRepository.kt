package com.yangian.numsum.core.firebase.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.yangian.numsum.core.constant.Constant.FIRESTORE_CALL_COLLECTION_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultFirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreRepository {
    override suspend fun uploadCallLogs(
        receiverID: String,
        hashMap: HashMap<String, Any>
    ) {
        try {
            withContext(Dispatchers.IO) {
                firestore.collection(FIRESTORE_CALL_COLLECTION_NAME)
                    .document(receiverID)
                    .set(hashMap, SetOptions.merge())
            }
        } catch (e: Exception) {
            Log.e("FIRESTORE", e.message.toString())
        }
    }

    override suspend fun isCallLogsArrayEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getSenderId(): String {
        TODO("Not yet implemented")
    }
}