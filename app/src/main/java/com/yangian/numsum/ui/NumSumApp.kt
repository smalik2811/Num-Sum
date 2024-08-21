package com.yangian.numsum.ui

import android.content.Context
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import com.yangian.numsum.navigation.NumSumAppNavHost
import com.yangian.numsum.navigation.NumSumDestination

@Composable
fun NumSumApp(
    windowSizeClass: WindowSizeClass,
    appState: NumSumAppState = rememberNumSumAppState(windowSizeClass = windowSizeClass),
    appContext: Context,
    startDestination: NumSumDestination,
) {
    NumSumAppNavHost(
        appState = appState,
        appContext = appContext,
        windowSizeClass = windowSizeClass,
        startDestination = startDestination,
    )
}