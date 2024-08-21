package com.yangian.numsum.navigation

import android.content.Context
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
    appContext: Context,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    startDestination: NumSumDestination
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = NumSumDestination.Calculator.route,
        modifier = modifier
    ) {

        calculatorScreen(
            windowSizeClass = windowSizeClass,
            navigateToLockedScreen = {
                appState.navigateToDestination(startDestination)
            }
        )

        temporaryScreen()

        onBoardScreen(
            windowSizeClass = windowSizeClass,
            appContext = appContext,
            navigateToCalculator = {
                appState.navigateToDestination(NumSumDestination.Calculator)
            },
            navigateToTemporary = {
                appState.navigateToDestination(NumSumDestination.Temporary)
            }
        )
    }
}