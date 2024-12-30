package com.yangian.numsum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.android.gms.ads.MobileAds
import com.yangian.numsum.core.data.util.NetworkMonitor
import com.yangian.numsum.core.designsystem.theme.AppTheme
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().setKeepOnScreenCondition {
            !mainViewModel.isSplashVisible.value
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
            ),
        )

        super.onCreate(savedInstanceState)
        MobileAds.initialize(this@MainActivity)

        setContent {
            val startDestination by mainViewModel.startDestination
            val appState = rememberNumSumAppState(
                networkMonitor = networkMonitor,
            )

            AppTheme {
                Surface {
                    NumSumApp(
                        windowSizeClass = calculateWindowSizeClass(activity = this),
                        appState = appState,
                        startDestination = startDestination,
                    )
                }
            }
        }

    }
}