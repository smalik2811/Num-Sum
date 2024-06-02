package com.yangian.numsum.feature.onboard.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yangian.numsum.feature.onboard.ui.OnBoardRoute

const val ONBOARD_ROUTE = "onBoard"

fun NavGraphBuilder.onBoardScreen(
    windowSizeClass: WindowSizeClass,
    navigateToCalculator: () -> Unit,
    navigateToTemporary: () -> Unit,
) {
    composable(route = ONBOARD_ROUTE) {
        OnBoardRoute(
            navigateToCalculator = navigateToCalculator,
            navigateToTemporary = navigateToTemporary,
        )
    }
}