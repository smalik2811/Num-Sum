package com.yangian.numsum.navigation

sealed class NumSumDestination(
    val route: String
) {
    data object Calculator: NumSumDestination(
        route = "calculator"
    )
    data object Temporary: NumSumDestination(
        route = "temporary"
    )
    data object OnBoard: NumSumDestination(
        route = "onBoard"
    )
}