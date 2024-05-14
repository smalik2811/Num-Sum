package com.yangian.numsum.feature.temporary

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
            val db = Firebase.firestore

            val callList: List<CallResource> = callResourcesRepository.getCalls().first()
            val map = hashMapOf<String, List<CallResource>>()
            map["Realme-Updated"] = callList

            // Add a new document with a generated ID
            db.collection("data")
                .document("call-logs")
                .set(map, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d(TAG, "Document added.")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
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