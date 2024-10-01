package com.yangian.numsum.core.firebase.repository

import android.content.Context
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions

interface FirestoreRepository {

    suspend fun getFirestoreDocument(
        collectionPath: String,
        documentPath: String
    ): DocumentSnapshot

    suspend fun setFirestoreDocument(
        collectionPath: String,
        documentPath: String,
        data: Map<String, Any>,
        options: SetOptions
    )

    suspend fun getHandShakeEncryptionKey(): ByteArray

    suspend fun validateReceiver(
        scannedReceiverId: String,
        firebaseUser: String,
    )

    suspend fun addData(
        senderId: String,
        receiverId: String,
        context: Context
    ): FirestoreResult
}