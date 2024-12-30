package com.yangian.numsum.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yangian.numsum.core.data.util.NetworkMonitor
import com.yangian.numsum.navigation.NumSumDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberNumSumAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    networkMonitor: NetworkMonitor
): NumSumAppState {
    return remember(navController) {
        NumSumAppState(
            navController,
            coroutineScope,
            networkMonitor
        )
    }
}

@Stable
class NumSumAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {

    private val numSumDestinations: List<NumSumDestination> = listOf(
        NumSumDestination.Calculator,
        NumSumDestination.OnBoard,
        NumSumDestination.Home,
    )

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun navigateToDestination(destination: NumSumDestination) {
        when (destination) {
            NumSumDestination.Calculator -> navController.navigate(NumSumDestination.Calculator.route) {
                popUpTo(NumSumDestination.Calculator.route) {
                    inclusive = true
                }
            }
            NumSumDestination.OnBoard -> {navController.navigate(NumSumDestination.OnBoard.route)}
            NumSumDestination.Home -> {
                navController.navigate(NumSumDestination.Home.route) {
                    popUpTo(NumSumDestination.OnBoard.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

}