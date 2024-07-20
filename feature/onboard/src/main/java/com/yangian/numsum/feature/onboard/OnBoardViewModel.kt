package com.yangian.numsum.feature.onboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.workmanager.UploadWorkerScheduler
import com.yangian.numsum.feature.onboard.model.OnBoardingScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    privateFirebaseAuth: FirebaseAuth,
    privateFirebaseFirestore: FirebaseFirestore
) : ViewModel() {

    private val _currentScreen =
        MutableStateFlow(OnBoardingScreens.TermsOfService)
    val currentScreen: StateFlow<OnBoardingScreens> = _currentScreen
    val isOnboardingCompleted = userPreferences.getOnboardingDone()

    val firebaseAuth = privateFirebaseAuth
    val firebaseFirestore = privateFirebaseFirestore

    fun navigateToNextScreen() {
        viewModelScope.launch {
            when (_currentScreen.value) {
                OnBoardingScreens.TermsOfService -> _currentScreen.value = OnBoardingScreens.Welcome
                OnBoardingScreens.Welcome -> _currentScreen.value = OnBoardingScreens.Connection1
                OnBoardingScreens.Connection1 -> _currentScreen.value =
                    OnBoardingScreens.Connection2

                OnBoardingScreens.Connection2 -> {}
            }
        }
    }

    fun navigateToPreviousScreen() {
        viewModelScope.launch {
            when (_currentScreen.value) {
                OnBoardingScreens.TermsOfService -> {}
                OnBoardingScreens.Welcome -> _currentScreen.value = OnBoardingScreens.TermsOfService
                OnBoardingScreens.Connection1 -> _currentScreen.value = OnBoardingScreens.Welcome
                OnBoardingScreens.Connection2 -> _currentScreen.value =
                    OnBoardingScreens.Connection1
            }
        }
    }

    fun updateReceiverId(newReceiverId: String) {
        viewModelScope.launch {
            userPreferences.setReceiverId(newReceiverId)
        }
    }

    fun updateOnBoardingCompleted(newOnboardingState: Boolean) {
        viewModelScope.launch {
            userPreferences.setOnboardingDone(newOnboardingState)
        }
    }

    fun registerLogsUploadWorkRequest(context: Context) {
        viewModelScope.launch {

            val workRequest = PeriodicWorkRequestBuilder<UploadWorkerScheduler>(
                repeatInterval = 2,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            ).build()

            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniquePeriodicWork(
                "UPLOAD_CALL_LOGS_SCHEDULER",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
        }
    }
}