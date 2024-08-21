package com.yangian.numsum.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yangian.numsum.navigation.NumSumDestination

@Composable
fun rememberNumSumAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
): NumSumAppState {
    return remember(navController, windowSizeClass) {
        NumSumAppState(navController)
    }
}

@Stable
class NumSumAppState (
    val navController: NavHostController,
) {

    private val numSumDestinations: List<NumSumDestination> = listOf(
        NumSumDestination.Calculator,
        NumSumDestination.OnBoard,
        NumSumDestination.Temporary,
    )

    fun navigateToDestination(destination: NumSumDestination) {
        when (destination) {
            NumSumDestination.Calculator -> navController.navigate(NumSumDestination.Calculator.route)
            NumSumDestination.OnBoard -> navController.navigate(NumSumDestination.OnBoard.route)
            NumSumDestination.Temporary -> navController.navigate(NumSumDestination.Temporary.route)
        }
    }

}