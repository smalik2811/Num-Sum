package com.yangian.numsum.ui

import android.content.Context
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.yangian.numsum.navigation.NumSumAppNavHost
import com.yangian.numsum.navigation.NumSumDestination

@Composable
fun NumSumApp(
    windowSizeClass: WindowSizeClass,
    appState: NumSumAppState,
    appContext: Context,
    startDestination: NumSumDestination,
) {
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    if (isOffline) {
        OfflineScreen()
    } else {
        NumSumAppNavHost(
            appState = appState,
            appContext = appContext,
            windowSizeClass = windowSizeClass,
            startDestination = startDestination,
        )
    }
}