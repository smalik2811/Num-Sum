package com.yangian.numsum.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yangian.numsum.core.constant.Constant.LAST_CALL_ID
import com.yangian.numsum.core.constant.Constant.LAST_UPLOADED_TIMESTAMP
import com.yangian.numsum.core.constant.Constant.LOGS_ENCRYPTION_KEY_PREFERENCE_KEY
import com.yangian.numsum.core.constant.Constant.ONBOARDING_DONE
import com.yangian.numsum.core.constant.Constant.RECEIVER_ID
import com.yangian.numsum.core.constant.Constant.WORKER_RETRY_POLICY_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val LAST_CALL_ID_KEY = longPreferencesKey(LAST_CALL_ID)
        val RECEIVER_ID_KEY = stringPreferencesKey(RECEIVER_ID)
        val ONBOARDING_DONE_KEY = booleanPreferencesKey(ONBOARDING_DONE)
        val LOGS_ENCRYPTION_KEY = stringPreferencesKey(LOGS_ENCRYPTION_KEY_PREFERENCE_KEY)
        val LAST_UPLOADED_TIMESTAMP_KEY = stringPreferencesKey(LAST_UPLOADED_TIMESTAMP)
        val WorkerRetry_Policy_Key = longPreferencesKey(WORKER_RETRY_POLICY_KEY)
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it.clear()
            }
        }
    }

    fun getLastCallId(): Flow<Long> {
        return dataStore.data.map {
            it[LAST_CALL_ID_KEY] ?: -1
        }
    }

    suspend fun setLastCallId(
        newLastCallId: Long
    ) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it[LAST_CALL_ID_KEY] = newLastCallId
            }
        }
    }

    fun getReceiverId(): Flow<String> {
        return dataStore.data.map {
            it[RECEIVER_ID_KEY] ?: ""
        }
    }

    suspend fun setReceiverId(
        newReceiverId: String
    ) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it[RECEIVER_ID_KEY] = newReceiverId
            }
        }
    }

    fun getOnboardingDone(): Flow<Boolean> {
        return dataStore.data.map {
            it[ONBOARDING_DONE_KEY] ?: false
        }
    }

    suspend fun setOnboardingDone(
        newOnboardingDone: Boolean
    ) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it[ONBOARDING_DONE_KEY] = newOnboardingDone
            }
        }
    }

    fun getLogsEncryptionKey(): Flow<String?> {
        return dataStore.data.map {
            it[LOGS_ENCRYPTION_KEY]
        }
    }

    suspend fun setLogsEncryptionKey(
        newLogsEncryptionKey: String
    ) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it[LOGS_ENCRYPTION_KEY] = newLogsEncryptionKey
            }
        }
    }

    fun getLastUploadedTimeStamp(): Flow<String?> {
        return dataStore.data.map {
            it[LAST_UPLOADED_TIMESTAMP_KEY]
        }
    }

    suspend fun setLastUploadedTimeStamp(
        newLastUploadedTimeStamp: String
    ) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it[LAST_UPLOADED_TIMESTAMP_KEY] = newLastUploadedTimeStamp
            }
        }
    }

    fun getWorkerRetryPolicy(): Flow<Long> {
        return dataStore.data.map {
            it[WorkerRetry_Policy_Key] ?: 6 // Default value of 6 hours
        }
    }

    suspend fun setWorkerRetryPolicy(
        newWorkerRetryPolicy: Long
    ) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it[WorkerRetry_Policy_Key] = newWorkerRetryPolicy
            }
        }
    }
}
