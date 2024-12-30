package com.yangian.numsum.feature.onboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yangian.numsum.feature.onboard.ui.OnBoardRoute

const val ONBOARD_ROUTE = "onBoard"

fun NavGraphBuilder.onBoardScreen(
    navigateToCalculator: () -> Unit,
    navigateToHome: () -> Unit,
) {
    composable(route = ONBOARD_ROUTE) {
        OnBoardRoute(
            navigateToCalculator = navigateToCalculator,
            navigateToHome = navigateToHome,
        )
    }
}