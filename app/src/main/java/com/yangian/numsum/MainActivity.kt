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
import androidx.lifecycle.lifecycleScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.ads.MobileAds
import com.yangian.numsum.core.data.util.NetworkMonitor
import com.yangian.numsum.core.datastore.UserPreferences
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.core.workmanager.LogsUploadWorker
import com.yangian.numsum.ui.NumSumApp
import com.yangian.numsum.ui.rememberNumSumAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var userPreferences: UserPreferences

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

        lifecycleScope.launch { ->
            val isOnboardingDone = userPreferences.getOnboardingDone().first()
            if (isOnboardingDone) {
                val existingWorkPolicy = userPreferences.getWorkerRetryPolicy().first()

                val workerConstraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val workRequest = PeriodicWorkRequestBuilder<LogsUploadWorker>(
                    repeatInterval = existingWorkPolicy,
                    repeatIntervalTimeUnit = TimeUnit.MINUTES,
                ).setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    2,
                    TimeUnit.MINUTES,
                ).setConstraints(
                    workerConstraints
                ).build()

                val workManager = WorkManager.getInstance(this@MainActivity)
                workManager.enqueueUniquePeriodicWork(
                    "LOGS_DOWNLOAD_WORKER",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
            }
        }

    }
}