package com.yangian.numsum.feature.onboard.navigation

import android.content.Context
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yangian.numsum.feature.onboard.ui.OnBoardRoute

const val ONBOARD_ROUTE = "onBoard"

fun NavGraphBuilder.onBoardScreen(
    windowSizeClass: WindowSizeClass,
    appContext: Context,
    navigateToCalculator: () -> Unit,
    navigateToTemporary: () -> Unit,
) {
    composable(route = ONBOARD_ROUTE) {
        OnBoardRoute(
            appContext = appContext,
            navigateToCalculator = navigateToCalculator,
            navigateToTemporary = navigateToTemporary,
        )
    }
}