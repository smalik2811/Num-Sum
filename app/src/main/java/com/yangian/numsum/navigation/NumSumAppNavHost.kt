package com.yangian.numsum.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yangian.numsum.feature.calculator.navigation.calculatorScreen
import com.yangian.numsum.feature.onboard.navigation.onBoardScreen
import com.yangian.numsum.feature.temporary.navigation.temporaryScreen
import com.yangian.numsum.ui.NumSumAppState

@Composable
fun NumSumAppNavHost(
    appState: NumSumAppState,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    startDestination: String
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        calculatorScreen(
            windowSizeClass = windowSizeClass,
        )

        temporaryScreen()

        onBoardScreen(
            windowSizeClass = windowSizeClass,
            navigateToCalculator = {
                appState.navigateToDestination(NumSumDestination.Calculator)
            },
            navigateToTemporary = {
                appState.navigateToDestination(NumSumDestination.Temporary)
            }
        )
    }
}