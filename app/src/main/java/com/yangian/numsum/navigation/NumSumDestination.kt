package com.yangian.numsum.navigation

sealed class NumSumDestination(
    val route: String
) {
    data object Calculator: NumSumDestination(
        route = "calculator"
    )
    data object Home: NumSumDestination(
        route = "home"
    )
    data object OnBoard: NumSumDestination(
        route = "onBoard"
    )
}