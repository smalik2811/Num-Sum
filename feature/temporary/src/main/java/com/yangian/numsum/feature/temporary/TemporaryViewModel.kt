package com.yangian.numsum.feature.temporary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yangian.numsum.core.data.repository.CallResourceRepository
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.model.CallResource
import com.yangian.numsum.core.ui.CallFeedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    val lastCallId: Flow<Long> = userPreferences.getLastCallId()
    val TAG = "Firebase"

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

    fun uploadLogsToFirestore() {
        viewModelScope.launch(Dispatchers.IO) {

            val currentUser = firebaseAuth.currentUser
            val receiverId = userPreferences.getReceiverId().first()
            val documentRef  = firestore.collection("call-logs").document(receiverId)

            documentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val senderId = documentSnapshot.getString("sender_id")
                        val existingLogsArray = documentSnapshot.get("logs_array")

                        if (currentUser != null) {
                            if (senderId == currentUser.uid && existingLogsArray == null) {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val callList: List<CallResource> = callResourcesRepository.getCalls().first()

                                    documentRef.update("logs-array", callList)
                                }
                            }
                        }
                    }
                }
        }
    }

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
}