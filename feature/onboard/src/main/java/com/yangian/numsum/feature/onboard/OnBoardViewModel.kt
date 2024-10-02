package com.yangian.numsum.feature.onboard

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.firebase.repository.FirestoreRepository
import com.yangian.numsum.core.model.cryptography.CryptoHandler
import com.yangian.numsum.core.network.model.DkmaManufacturer
import com.yangian.numsum.core.network.retrofit.RetrofitDkmaNetwork
import com.yangian.numsum.core.workmanager.LogsUploadWorker
import com.yangian.numsum.feature.onboard.model.OnBoardingScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import qrcode.QRCode
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed interface DkmaUiState {
    data class Success(
        val dkmaManufacturer: DkmaManufacturer
    ) : DkmaUiState

    data object Error : DkmaUiState
    data object Loading : DkmaUiState
}

sealed interface HandShakeUI {
    data object Cold : HandShakeUI
    data object Loading : HandShakeUI
    data object Error : HandShakeUI
    data class Success(
        val qrCode: ImageBitmap
    ) : HandShakeUI
}

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val firebaseAuth: FirebaseAuth,
    private val firestoreRepository: FirestoreRepository,
    private val dataSource: RetrofitDkmaNetwork
) : ViewModel() {

    // UI
    private val _currentScreen =
        MutableStateFlow(OnBoardingScreens.TermsOfService)
    val currentScreen: StateFlow<OnBoardingScreens> = _currentScreen

    fun navigateToNextScreen() {
        viewModelScope.launch {
            when (_currentScreen.value) {
                OnBoardingScreens.TermsOfService -> _currentScreen.value = OnBoardingScreens.Welcome
                OnBoardingScreens.Welcome -> _currentScreen.value = OnBoardingScreens.DkmaScreen
                OnBoardingScreens.DkmaScreen -> _currentScreen.value = OnBoardingScreens.Connection1
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
                OnBoardingScreens.DkmaScreen -> _currentScreen.value = OnBoardingScreens.Welcome
                OnBoardingScreens.Connection1 -> _currentScreen.value = OnBoardingScreens.DkmaScreen
                OnBoardingScreens.Connection2 -> _currentScreen.value =
                    OnBoardingScreens.Connection1
            }
        }
    }

    // Firebase
    private var firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    fun createFirebaseAccount() {
        firebaseAuth.signInAnonymously()
    }

    private fun getFirebaseUser(): String {
        if (firebaseUser == null) {
            createFirebaseAccount()
        }
        firebaseUser = firebaseAuth.currentUser
        return firebaseUser!!.uid
    }

    // Firestore
    @Throws
    private suspend fun getEncryptedHandShakeString(): String {

        val handShakeEncryptionKey = firestoreRepository.getHandShakeEncryptionKey()
        Log.i("GM", "HandShakeEncryption Key fetched from Firestore.")
        val cryptoHandler = CryptoHandler()
        val keyword = "yangian"
        val timeStampString = System.currentTimeMillis().toString().padStart(19, '0')
        val logsEncryptionKey: String = cryptoHandler.generateKey()
        val handShakeString =
            keyword + timeStampString + logsEncryptionKey + getFirebaseUser()
        Log.i("GM", "HandShake String prepared")
        setLogsEncryptionKey(logsEncryptionKey)
        Log.i("GM", "Encrypting HandShake Key")
        val encryptedHandShakeString: String =
            cryptoHandler.getEncrypter(handShakeEncryptionKey)
                .encryptString(handShakeString)
        Log.i("GM", "HandShakeString Encrypted")
        return encryptedHandShakeString
    }


    // DKMA
    private val manufacturerName = Build.MANUFACTURER
    var isIssueVisible by mutableStateOf(false)
        private set
    var isSolutionVisible by mutableStateOf(false)
        private set
    var dkmaUiState: DkmaUiState by mutableStateOf(DkmaUiState.Loading)

    fun loadDkmaManufacturer() {
        viewModelScope.launch {
            dkmaUiState = try {
                DkmaUiState.Success(
                    dkmaManufacturer = dataSource.getDkmaManufacturer(
                        manufacturerName
                    )
                )
            } catch (e: Exception) {
                Log.e("DkmaViewModel", "Error loading DKMA Manufacturer", e)
                DkmaUiState.Error
            }
        }
    }

    fun alterIssueVisibility() {
        isIssueVisible = !isIssueVisible
    }


    fun alterSolutionVisibility() {
        isSolutionVisible = !isSolutionVisible
    }

    // User Preferences
    private fun setOnBoardingCompleted() {
        viewModelScope.launch {
            userPreferences.setOnboardingDone(true)
        }
    }

    private fun setLogsEncryptionKey(
        newLogsEncryptionKey: String
    ) {
        viewModelScope.launch {
            userPreferences.setLogsEncryptionKey(newLogsEncryptionKey)
        }
        Log.i("GM", "Encryption Key stored in Preferences.")
    }

    private fun setReceiverId(newReceiverId: String) {
        viewModelScope.launch {
            userPreferences.setReceiverId(newReceiverId)
        }
    }


    // QR Code
    var handShakeUIState: HandShakeUI by mutableStateOf(HandShakeUI.Cold)

    fun turnHandShakeUICold() {
        handShakeUIState = HandShakeUI.Cold
    }

    fun prepareQrCode(
        backgroundColor: Int,
        foregroundColor: Int
    ) {
        handShakeUIState = HandShakeUI.Loading
        Log.i("GM", "HandShakeUIState changed to Loading.")
        viewModelScope.launch {
            try {
                val encryptedHandShakeString = getEncryptedHandShakeString()
                Log.i("GM", "EncryptedHandShakeString fetched.")
                val qrCode = getQrCode(backgroundColor, foregroundColor, encryptedHandShakeString)
                Log.i("GM", "QR code generated, handshake ui state changed to Success.")
                handShakeUIState = HandShakeUI.Success(qrCode)
            } catch (exception: Exception) {
                handShakeUIState = HandShakeUI.Error
            }
        }
    }

    private fun getQrCode(
        backgroundColor: Int,
        foregroundColor: Int,
        handShakeEncryptedString: String
    ): ImageBitmap {
        val byteArray: ByteArray = QRCode
            .ofSquares()
            .withBackgroundColor(backgroundColor)
            .withColor(foregroundColor)
            .build(handShakeEncryptedString)
            .renderToBytes()

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    }

    private fun registerLogsUploadWorkRequest(context: Context) {
        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<LogsUploadWorker>(
            repeatInterval = 6,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).setConstraints(workerConstraints)
            .build()

        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniquePeriodicWork(
            "LOGS_UPLOAD_WORKER",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    fun validateReceiver(
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit
    ) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                firestoreRepository.validateReceiver(scannedReceiverId, getFirebaseUser())
                handleSuccessfulScan(scannedReceiverId, context, navigateToHome)
            }
        } catch (exception: Exception) {
            Log.e("GM", "Error validating receiver", exception)
            navigateToPreviousScreen()
        }
    }

    private fun handleSuccessfulScan(
        receiverId: String,
        context: Context,
        navigateToHome: () -> Unit
    ) {
        viewModelScope.launch {
            setReceiverId(receiverId)
            registerLogsUploadWorkRequest(context)
            setOnBoardingCompleted()
            navigateToHome()
        }
    }


    init {
        loadDkmaManufacturer()
    }
}