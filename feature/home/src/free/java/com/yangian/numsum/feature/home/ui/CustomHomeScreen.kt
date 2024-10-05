package com.yangian.numsum.feature.home.ui

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.google.android.gms.ads.nativead.NativeAd
import com.yangian.numsum.core.designsystem.component.CustomAlertDialog
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.component.admob.AdMobBannerExpanded
import com.yangian.numsum.core.designsystem.component.admob.CallNativeAd
import com.yangian.numsum.core.designsystem.component.admob.loadNativeAd
import com.yangian.numsum.core.designsystem.icon.CheckCircleIcon
import com.yangian.numsum.core.designsystem.icon.LogoutIcon
import com.yangian.numsum.core.designsystem.icon.MoreVertIcon
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.core.designsystem.theme.extendedDark
import com.yangian.numsum.core.designsystem.theme.extendedLight
import com.yangian.numsum.feature.home.BuildConfig
import com.yangian.numsum.feature.home.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHomeScreen(
    isMenuVisible: Boolean,
    isSignOutDialogVisible: Boolean,
    lastUploadedTimestamp: String?,
    showMenu: () -> Unit,
    hideMenu: () -> Unit,
    showSignOutDialog: () -> Unit,
    hideSignOutDialog: () -> Unit,
    uploadLogs: (context: Context) -> Unit,
    navigateToCalculator: () -> Unit,
    signOut: (context: Context, navigateToOnboarding: () -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    LaunchedEffect(null) {
        loadNativeAd(context, BuildConfig.NativeAdUnitId) {
            nativeAd = it
        }
    }
    val configuration = LocalConfiguration.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                },
                actions = {
                    IconButton(
                        onClick = showMenu
                    ) {
                        Icon(imageVector = MoreVertIcon, stringResource(R.string.menu))
                    }

                    DropdownMenu(
                        expanded = isMenuVisible,
                        onDismissRequest = hideMenu,
                        modifier = Modifier
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(R.string.sign_out)
                                )
                            },
                            onClick = showSignOutDialog
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                AdMobBannerExpanded()
            } else if (nativeAd != null) {
                CallNativeAd(nativeAd!!)
            }
        },
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    it
                ),
        ) {
            Icon(
                imageVector = CheckCircleIcon,
                contentDescription = stringResource(R.string.all_done),
                tint = when (isSystemInDarkTheme()) {
                    true -> extendedDark.success.color
                    false -> extendedLight.success.color
                },
                modifier = Modifier
                    .size(dimensionResource(com.yangian.numsum.core.designsystem.R.dimen.icon_size_large))
            )

            Spacer(
                modifier = Modifier.height(
                    dimensionResource(
                        com.yangian.numsum.core.designsystem.R.dimen.padding_medium
                    )
                )
            )

            Text(
                text = stringResource(R.string.all_done),
                style = MaterialTheme.typography.headlineMedium
            )

            if (!lastUploadedTimestamp.isNullOrEmpty()) {
                Spacer(
                    modifier = Modifier.height(
                        dimensionResource(
                            com.yangian.numsum.core.designsystem.R.dimen.padding_large
                        )
                    )
                )

                Text(
                    text = "Last uploaded on $lastUploadedTimestamp",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    AnimatedVisibility(isSignOutDialogVisible) {
        CustomAlertDialog(
            onDismissRequest = hideSignOutDialog,
            onNegativeButtonClick = hideSignOutDialog,
            onPositiveButtonClick = {
                signOut(
                    context,
                    navigateToCalculator
                )
            },
            dialogTitle = stringResource(R.string.sign_out_dialog_title),
            dialogText = stringResource(R.string.sign_out_dialog_description),
            icon = LogoutIcon,
            positiveButtonText = stringResource(R.string.yes),
            negativeButtonText = stringResource(R.string.cancel),
            iconContentDescriptionText = stringResource(R.string.logout)
        )
    }
}

@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE, device = "id:pixel_8_pro")
@Composable
private fun CallLogsDisplayPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
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