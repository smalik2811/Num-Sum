package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yangian.numsum.core.designsystem.component.CustomAlertDialog
import com.yangian.numsum.core.designsystem.component.DkmaView
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.icon.ErrorIcon
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.core.network.model.DkmaManufacturer
import com.yangian.numsum.feature.onboard.DkmaUiState
import com.yangian.numsum.feature.onboard.R
import kotlin.system.exitProcess

@Composable
fun DkmaScreen(
    dkmaUiState: DkmaUiState,
    isIssueVisible: Boolean,
    isSolutionVisible: Boolean,
    alterIssueVisibility: () -> Unit,
    alterSolutionVisibility: () -> Unit,
    retryDkmaLoading: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val permissionArray = mutableListOf<String>()

    val isCallLogPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    if (!isCallLogPermissionGranted) {
        permissionArray.add(Manifest.permission.READ_CALL_LOG)
    }

    val isCameraPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    if (!isCameraPermissionGranted) {
        permissionArray.add(Manifest.permission.CAMERA)
    }

    if (permissionArray.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            context as Activity,
            permissionArray.toTypedArray(),
            1
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {

        when (dkmaUiState) {
            is DkmaUiState.Success -> {
                DkmaView(
                    dkmaManufacturer = dkmaUiState.dkmaManufacturer,
                    isIssueVisible,
                    isSolutionVisible,
                    alterIssueVisibility,
                    alterSolutionVisibility,
                    modifier.weight(1f)
                )
            }

            is DkmaUiState.Error -> {
                DkmaErrorScreen(retryDkmaLoading)
            }

            is DkmaUiState.Loading -> {
                DkmaLoadingScreen(modifier = modifier)
            }
        }
    }
}

@Composable
private fun DkmaLoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DkmaErrorScreen(
    retryDkmaLoading: () -> Unit,
    modifier: Modifier = Modifier
) {
    CustomAlertDialog(
        onNegativeButtonClick = {
            exitProcess(0)
        },
        onPositiveButtonClick = retryDkmaLoading,
        onDismissRequest = {},
        dialogTitle = stringResource(R.string.something_went_wrong),
        dialogText = stringResource(R.string.dkma_error_dialog_message),
        positiveButtonText = stringResource(R.string.retry),
        negativeButtonText = stringResource(R.string.exit_app),
        iconContentDescriptionText = stringResource(R.string.error_icon),
        icon = ErrorIcon,
        modifier = modifier
    )
}

@Preview
@Composable
private fun DkmaErrorScreenPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            DkmaErrorScreen(
                retryDkmaLoading = {}
            )
        }
    }
}

@Preview
@Composable
private fun DkmaLoadingScreenPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            DkmaLoadingScreen(
                Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun DkmaPreviewLoading() {
    NumSumAppTheme {
        NumSumAppBackground {
            DkmaScreen(
                dkmaUiState = DkmaUiState.Loading,
                isIssueVisible = false,
                isSolutionVisible = false,
                alterIssueVisibility = {},
                alterSolutionVisibility = {},
                retryDkmaLoading = {},
            )
        }
    }
}

@Preview
@Composable
private fun DkmaPreviewError() {
    NumSumAppTheme {
        NumSumAppBackground {
            DkmaScreen(
                dkmaUiState = DkmaUiState.Error,
                isIssueVisible = false,
                isSolutionVisible = false,
                alterIssueVisibility = {},
                alterSolutionVisibility = {},
                retryDkmaLoading = {},
            )
        }
    }
}

@Preview
@Composable
private fun DkmaPreviewSuccess() {
    val dummyData: DkmaManufacturer = DkmaManufacturer(
        explanation = stringResource(R.string.dkma_dummy_explanation),
        user_solution = stringResource(R.string.dkma_dummy_user_solution),
    )
    NumSumAppTheme {
        NumSumAppBackground {
            var issueVisibility by remember { mutableStateOf(false) }
            var solutionVisibility by remember { mutableStateOf(false) }
            DkmaScreen(
                dkmaUiState = DkmaUiState.Success(dummyData),
                isIssueVisible = issueVisibility,
                isSolutionVisible = solutionVisibility,
                alterIssueVisibility = { issueVisibility = !issueVisibility },
                alterSolutionVisibility = { solutionVisibility = !solutionVisibility },
                modifier = Modifier.fillMaxSize(),
                retryDkmaLoading = {}
            )
        }
    }
}