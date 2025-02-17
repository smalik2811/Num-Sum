package com.yangian.numsum

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.navigation.NumSumDestination
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _isSplashVisible: MutableState<Boolean> = mutableStateOf(true)
    val isSplashVisible: MutableState<Boolean> = _isSplashVisible

    private val _startDestination: MutableState<NumSumDestination> = mutableStateOf(NumSumDestination.OnBoard)
    val startDestination: State<NumSumDestination> = _startDestination

    init {
        viewModelScope.launch {
            userPreferences.getOnboardingDone().collect() { completed ->
                if (completed) {
                    _startDestination.value = NumSumDestination.Home
                } else {
                    _startDestination.value = NumSumDestination.OnBoard
                }
            }
            _isSplashVisible.value = false
        }
    }
}