package com.yangian.numsum.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yangian.numsum.feature.calculator.navigation.calculatorScreen
import com.yangian.numsum.feature.home.navigation.homeScreen
import com.yangian.numsum.feature.onboard.navigation.onBoardScreen
import com.yangian.numsum.ui.NumSumAppState

@Composable
fun NumSumAppNavHost(
    appState: NumSumAppState,
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
            navigateToOnboardingScreen = {
                appState.navigateToDestination(startDestination)
            }
        )

        homeScreen(navigateToCalculator = {
            appState.navigateToDestination(NumSumDestination.Calculator)
        })

        onBoardScreen(
            navigateToCalculator = {
                appState.navigateToDestination(NumSumDestination.Calculator)
            },
            navigateToHome = {
                appState.navigateToDestination(NumSumDestination.Home)
            }
        )
    }
}