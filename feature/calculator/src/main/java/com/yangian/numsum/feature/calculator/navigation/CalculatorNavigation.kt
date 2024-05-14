package com.yangian.numsum.feature.calculator.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yangian.numsum.feature.calculator.ui.CalculatorRouteCompact
import com.yangian.numsum.feature.calculator.ui.CalculatorRouteExpanded
import com.yangian.numsum.feature.calculator.ui.CalculatorRouteMedium

const val CALCULATOR_ROUTE = "calculator"
fun NavGraphBuilder.calculatorScreen(
    windowSizeClass: WindowSizeClass
) {
    composable(route = CALCULATOR_ROUTE) {
        when(windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                CalculatorRouteCompact()
            }

            WindowWidthSizeClass.Medium -> {
                CalculatorRouteMedium()
            }

            WindowWidthSizeClass.Expanded -> {
                CalculatorRouteExpanded()
            }
        }
    }
}