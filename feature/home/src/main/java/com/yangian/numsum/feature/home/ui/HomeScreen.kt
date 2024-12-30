package com.yangian.numsum.feature.home.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.yangian.numsum.core.designsystem.component.AppBackground
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.feature.home.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navigateToCalculator: () -> Unit,
) {
    // Checking and requesting permission
    val localContext = LocalContext.current
    val lastUploadedTimestamp = homeViewModel.lastUploadedTimestamp.collectAsState(null)
    val isMenuVisible by homeViewModel.isMenuVisible.collectAsState()
    val isSignOutDialogVisible by homeViewModel.isSignOutDialogVisible.collectAsState()

    val isCallLogPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                localContext,
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    if (!isCallLogPermissionGranted) {
        // Permission is not granted, request it

        ActivityCompat.requestPermissions(
            localContext as Activity,
            arrayOf(Manifest.permission.READ_CALL_LOG),
            1
        )

        Text("Permission not granted to access Call Logs.")
    } else {
        CustomHomeScreen(
            isMenuVisible = isMenuVisible,
            isSignOutDialogVisible = isSignOutDialogVisible,
            lastUploadedTimestamp = lastUploadedTimestamp.value,
            showMenu = homeViewModel::showMenu,
            hideMenu = homeViewModel::hideMenu,
            showSignOutDialog = homeViewModel::showSignOutDialog,
            hideSignOutDialog = homeViewModel::hideSignOutDialog,
            uploadLogs = homeViewModel::uploadLogs,
            signOut = homeViewModel::signOut,
            navigateToCalculator = navigateToCalculator,
        )
    }
}

@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE, device = "id:pixel_8_pro")
@Composable
private fun CallLogsDisplayPreview() {
    AppTheme {
        AppBackground {
            CustomHomeScreen(
                isMenuVisible = false,
                isSignOutDialogVisible = false,
                lastUploadedTimestamp = "2 Oct, 2024",
                showMenu = {},
                hideMenu = {},
                showSignOutDialog = {},
                hideSignOutDialog = {},
                uploadLogs = {},
                navigateToCalculator = {},
                signOut = { _, _ -> }
            )
        }
    }
}