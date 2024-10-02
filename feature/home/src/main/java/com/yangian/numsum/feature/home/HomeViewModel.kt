package com.yangian.numsum.feature.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.yangian.numsum.core.data.repository.CallResourceRepository
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.firebase.repository.FirestoreRepository
import com.yangian.numsum.core.workmanager.LogsUploadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences,
    private val callResourcesRepository: CallResourceRepository,
) : ViewModel() {

    val lastUploadedTimestamp = userPreferences.getLastUploadedTimeStamp()

    private val _isSignOutDialogVisible = MutableStateFlow(false)
    val isSignOutDialogVisible = _isSignOutDialogVisible.value

    private val _isMenuVisible = MutableStateFlow(false)
    val isMenuVisible = _isMenuVisible.value

    fun signOut(
        context: Context,
        navigateToOnboarding: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            WorkManager.getInstance(context).cancelAllWork()
            val currentUserId: String = async { firebaseAuth.currentUser?.uid }.await().toString()
            val receiver = userPreferences.getReceiverId().first()
            firestoreRepository.deleteRecords(receiver)
            callResourcesRepository.deleteCalls()
            userPreferences.clear()

        }
    }

    fun showMenu() {
        _isMenuVisible.value = true
    }

    fun hideMenu() {
        _isMenuVisible.value = false
    }

    fun showSignOutDialog() {
        _isSignOutDialogVisible.value = true
    }

    fun hideSignOutDialog() {
        _isSignOutDialogVisible.value = false
    }

    fun uploadLogs(context: Context) {
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