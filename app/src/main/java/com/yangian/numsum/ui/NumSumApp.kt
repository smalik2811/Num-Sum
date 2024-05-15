package com.yangian.numsum.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.navigation.NumSumAppNavHost

@Composable
fun NumSumApp(
    windowSizeClass: WindowSizeClass,
    appState: NumSumAppState = rememberNumSumAppState(windowSizeClass = windowSizeClass)
) {
    NumSumAppTheme {
        NumSumAppBackground {
            NumSumAppNavHost(
                appState = appState,
                windowSizeClass = windowSizeClass
            )
        }
    }
}