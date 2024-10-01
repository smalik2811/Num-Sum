package com.yangian.numsum.core.firebase.repository

import android.content.Context
import android.provider.CallLog
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.yangian.numsum.core.data.repository.CallResourceRepository
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.model.CallResource
import com.yangian.numsum.core.model.cryptography.CryptoHandler
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

sealed interface FirestoreResult {
    data object Failure : FirestoreResult
    data object Success : FirestoreResult
    data object Retry : FirestoreResult
}

class DefaultFirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userPreferences: UserPreferences,
    private val callResourcesRepository: CallResourceRepository
) : FirestoreRepository {

    @Throws(Exception::class)
    override suspend fun getFirestoreDocument(
        collectionPath: String,
        documentPath: String
    ): DocumentSnapshot = suspendCoroutine { continuation ->
        val documentRef = firestore.collection(collectionPath).document(documentPath)
        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                continuation.resume(documentSnapshot)
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
    }

    @Throws(Exception::class)
    override suspend fun setFirestoreDocument(
        collectionPath: String,
        documentPath: String,
        data: Map<String, Any>,
        options: SetOptions
    ) {
        suspendCoroutine { continuation ->
            val documentRef = firestore.collection(collectionPath).document(documentPath)
            documentRef.set(data, options)
                .addOnSuccessListener {
                    continuation.resume(it)
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    @Throws(Exception::class)
    override suspend fun getHandShakeEncryptionKey(): ByteArray {
        val document = getFirestoreDocument(
            collectionPath = "key",
            documentPath = "handshake"
        )
        Log.i("GM", "Firestore Document fetched for HandShake Key.")

        val handShakeEncryptionKeyArray: List<*> = document.get("keys") as List<*>
        Log.i("GM", "Firestore HandShake Key fetched.")
        val handShakeEncryptionKeyString = handShakeEncryptionKeyArray[0].toString()
        val handShakeEncryptionKeyByteArray =
            CryptoHandler().hexStringToByteArray(handShakeEncryptionKeyString)
        return handShakeEncryptionKeyByteArray
    }

    @Throws
    override suspend fun validateReceiver(
        scannedReceiverId: String,
        firebaseUser: String
    ) {
        val document = getFirestoreDocument(
            collectionPath = "logs",
            documentPath = scannedReceiverId
        )
        if (!document.exists()) {
            throw FirebaseFirestoreException(
                "Document does not exist",
                FirebaseFirestoreException.Code.NOT_FOUND
            )
        }

        val cloudSenderIdValue: String = document.getString("sender")!!

        if (cloudSenderIdValue != firebaseUser) {
            throw FirebaseFirestoreException(
                "Sender ID mismatch.",
                FirebaseFirestoreException.Code.PERMISSION_DENIED
            )
        }

    }

    @Throws
    override suspend fun addData(
        senderId: String,
        receiverId: String,
        context: Context
    ): FirestoreResult {
        storeLogsInLocalDatabase(context)
        val callList = callResourcesRepository.getCalls().first()

        if (callList.isEmpty()) {
            return FirestoreResult.Success
        }

        val document = getFirestoreDocument("logs", receiverId)
        val data = mutableMapOf<String, Any>()

        if (!document.exists()) {
            return FirestoreResult.Failure
        }

        val cloudSenderId = document.getString("sender").toString()
        if (senderId != cloudSenderId) {
            return FirestoreResult.Failure
        }

        val cloudArray = document["array"] as List<*>
        if (cloudArray.isEmpty()) {
            val encryptionKeyString =
                userPreferences.getLogsEncryptionKey().first() ?: return FirestoreResult.Retry
            val cryptoHandler = CryptoHandler()
            val encryptionKeyByteArray = cryptoHandler.hexStringToByteArray(encryptionKeyString)
            val encrypter = cryptoHandler.getEncrypter(encryptionKeyByteArray).build()
            data["array"] = callList.map {
                encrypter.encryptString(it.toString())
            }
        }
        data["upload_timestamp"] = FieldValue.serverTimestamp()

        setFirestoreDocument(
            collectionPath = "logs",
            receiverId,
            data,
            SetOptions.merge()
        )

        callResourcesRepository.deleteCalls()
        return FirestoreResult.Success
    }


    private suspend fun getLastCallId(): Long = userPreferences.getLastCallId().first()

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
            callResourcesRepository.addCalls(listOfCallResource.toList())
            userPreferences.updateLastCallId(listOfCallResource.last().id)

        }
    }
}