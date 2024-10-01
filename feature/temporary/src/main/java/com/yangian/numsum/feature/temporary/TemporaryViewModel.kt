package com.yangian.numsum.feature.temporary

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yangian.numsum.core.data.repository.CallResourceRepository
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.model.CallResource
import com.yangian.numsum.core.ui.CallFeedUiState
import com.yangian.numsum.core.workmanager.LogsUploadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemporaryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences,
    private val callResourcesRepository: CallResourceRepository,
) : ViewModel() {

    private val _lastCallId = MutableStateFlow(-1L)
    val lastCallId: StateFlow<Long> = _lastCallId.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.getLastCallId().collect { newLastCallId ->
                _lastCallId.value = newLastCallId
            }
        }
    }

    fun updateLastCallId(newLastCallId: Long) {
        viewModelScope.launch {
            userPreferences.updateLastCallId(newLastCallId)
        }
    }

    val feedState: StateFlow<CallFeedUiState> = callResourcesRepository.getCalls()
        .map(CallFeedUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CallFeedUiState.Loading
        )

    fun addCalls(calls: List<CallResource>) {
        viewModelScope.launch {
            (Dispatchers.IO) {
                callResourcesRepository.addCalls(calls)
            }
        }
    }

    fun deleteCalls() {
        viewModelScope.launch {
            (Dispatchers.IO) {
                callResourcesRepository.deleteCalls()
            }
        }
    }

    suspend fun uploadLogsToFirestore(context: Context) {
        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<LogsUploadWorker>()
            .setConstraints(workerConstraints)
            .build()

        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniqueWork(
            "LOGS_UPLOAD_WORKER_OneTime",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}