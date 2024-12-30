package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yangian.numsum.core.designsystem.component.CustomAlertDialog
import com.yangian.numsum.core.designsystem.component.DkmaView
import com.yangian.numsum.core.designsystem.component.AppBackground
import com.yangian.numsum.core.designsystem.icon.ErrorIcon
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.core.network.model.DkmaManufacturer
import com.yangian.numsum.feature.onboard.DkmaUiState
import com.yangian.numsum.feature.onboard.R
import kotlin.system.exitProcess

@Composable
fun DkmaScreen(
    createFirebaseAccount: () -> Unit,
    dkmaUiState: DkmaUiState,
    isIssueVisible: Boolean,
    isSolutionVisible: Boolean,
    alterIssueVisibility: () -> Unit,
    alterSolutionVisibility: () -> Unit,
    retryDkmaLoading: () -> Unit,
    modifier: Modifier = Modifier
) {

    when (dkmaUiState) {
        is DkmaUiState.Success -> {
            DkmaView(
                dkmaManufacturer = dkmaUiState.dkmaManufacturer,
                isIssueVisible,
                isSolutionVisible,
                alterIssueVisibility,
                alterSolutionVisibility,
                modifier
            )
            createFirebaseAccount()
        }

        is DkmaUiState.Error -> {
            DkmaErrorScreen(retryDkmaLoading)
        }

        is DkmaUiState.Loading -> {
            DkmaLoadingScreen(modifier = modifier)
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

@Preview
@Composable
private fun DkmaLoadingScreenPreview() {
    AppTheme {
        AppBackground {
            DkmaLoadingScreen(
                Modifier.fillMaxSize()
            )
        }
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
    AppTheme {
        AppBackground {
            DkmaErrorScreen(
                retryDkmaLoading = {}
            )
        }
    }
}

@Preview
@Composable
private fun DkmaPreviewLoading() {
    AppTheme {
        AppBackground {
            AppBackground {
                Column {
                    DkmaScreen(
                        {},
                        dkmaUiState = DkmaUiState.Loading,
                        isIssueVisible = false,
                        isSolutionVisible = false,
                        alterIssueVisibility = {},
                        alterSolutionVisibility = {},
                        retryDkmaLoading = {}
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DkmaPreviewError() {
    AppTheme {
        AppTheme {
            AppBackground {
                Column {
                    DkmaScreen(
                        {},
                        dkmaUiState = DkmaUiState.Error,
                        isIssueVisible = false,
                        isSolutionVisible = false,
                        alterIssueVisibility = {},
                        alterSolutionVisibility = {},
                        retryDkmaLoading = {}
                    )
                }
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun DkmaPreviewSuccess() {
    val dummyData = DkmaManufacturer(
        explanation = stringResource(R.string.dkma_dummy_explanation),
        user_solution = stringResource(R.string.dkma_dummy_user_solution),
    )
    AppTheme {
        AppBackground {
            var issueVisibility by remember { mutableStateOf(false) }
            var solutionVisibility by remember { mutableStateOf(false) }
            Column {
                DkmaScreen(
                    {},
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
}