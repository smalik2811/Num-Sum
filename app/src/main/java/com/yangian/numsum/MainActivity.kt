package com.yangian.numsum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yangian.numsum.core.data.util.NetworkMonitor
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.ui.NumSumApp
import com.yangian.numsum.ui.rememberNumSumAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            !mainViewModel.isSplashVisible.value
        }


        setContent {
            val startDestination by mainViewModel.startDestination
            val appState = rememberNumSumAppState(
                networkMonitor = networkMonitor,
            )

            NumSumAppTheme {
                NumSumAppBackground {
                    NumSumApp(
                        windowSizeClass = calculateWindowSizeClass(activity = this),
                        appState = appState,
                        appContext = applicationContext,
                        startDestination = startDestination
                    )
                }
            }
        }

    }
}