package com.yangian.numsum.core.firebase.repository

interface FirestoreRepository {

    suspend fun uploadCallLogs(
        receiverID: String,
        hashMap: HashMap<String, Any>
    )

    suspend fun isCallLogsArrayEmpty(): Boolean

    suspend fun getSenderId(): String
}