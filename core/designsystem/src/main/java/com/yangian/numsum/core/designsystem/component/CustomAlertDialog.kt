package com.yangian.numsum.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.yangian.numsum.core.designsystem.icon.ErrorIcon
import com.yangian.numsum.core.designsystem.icon.LogoutIcon
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme

@Composable
fun CustomAlertDialog(
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    positiveButtonText: String,
    negativeButtonText: String,
    iconContentDescriptionText: String?,
    modifier: Modifier = Modifier,
    icon: ImageVector = ErrorIcon,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = iconContentDescriptionText)
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onPositiveButtonClick
            ) {
                Text(text = positiveButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onNegativeButtonClick
            ) {
                Text(text = negativeButtonText)
            }
        },
        modifier = modifier
    )
}

@Composable
fun CustomAlertDialog(
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    dialogTitle: String,
    dialogText: AnnotatedString,
    positiveButtonText: String,
    negativeButtonText: String,
    iconContentDescriptionText: String?,
    modifier: Modifier = Modifier,
    icon: ImageVector = ErrorIcon,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = iconContentDescriptionText)
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onPositiveButtonClick
            ) {
                Text(text = positiveButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onNegativeButtonClick
            ) {
                Text(text = negativeButtonText)
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun CustomAlertDialogPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            CustomAlertDialog(
                onDismissRequest = { },
                onPositiveButtonClick = { },
                onNegativeButtonClick = { },
                dialogTitle = "Are you sure you want to sign out?",
                dialogText = "Once signed out, you will no longer receive latest call logs and the existing logs will be deleted.",
                icon = LogoutIcon,
                positiveButtonText = "Sign out",
                negativeButtonText = "Cancel",
                iconContentDescriptionText = "Logout Icon",
            )
        }
    }
}