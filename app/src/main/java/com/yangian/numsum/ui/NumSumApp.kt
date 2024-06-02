package com.yangian.numsum.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import com.yangian.numsum.navigation.NumSumAppNavHost

@Composable
fun NumSumApp(
    windowSizeClass: WindowSizeClass,
    appState: NumSumAppState = rememberNumSumAppState(windowSizeClass = windowSizeClass),
    startDestination: String,
) {
    NumSumAppNavHost(
        appState = appState,
        windowSizeClass = windowSizeClass,
        startDestination = startDestination,
    )
}