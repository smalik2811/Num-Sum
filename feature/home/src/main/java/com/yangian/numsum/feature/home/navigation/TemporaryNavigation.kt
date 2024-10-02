package com.yangian.numsum.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yangian.numsum.feature.home.ui.HomeScreen

const val TEMPORARY_ROUTE = "home"

fun NavGraphBuilder.homeScreen(
    navigateToCalculator: () -> Unit,
) {
    composable(route = TEMPORARY_ROUTE) {
        HomeScreen(
            navigateToCalculator = navigateToCalculator
        )
    }
}