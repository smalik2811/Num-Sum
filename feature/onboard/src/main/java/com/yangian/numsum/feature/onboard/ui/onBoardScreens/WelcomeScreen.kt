package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.onboard.OnBoardViewModel
import com.yangian.numsum.feature.onboard.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onBoardViewModel: OnBoardViewModel,
    navigateToCalculator: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val somethingWentWrong = stringResource(R.string.something_went_wrong)
    val firebaseAuth = onBoardViewModel.firebaseAuth

    LaunchedEffect(Dispatchers.IO) {
        coroutineScope.launch {
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                firebaseAuth.signInAnonymously()
                    .addOnFailureListener {
                        println("Could not Sign in Anonymously: ${it.message}")
                        Toast.makeText(
                            context,
                            somethingWentWrong,
                            Toast.LENGTH_LONG
                        ).show()
                        navigateToCalculator()
                    }
            }
        }
    }

    val localContext = LocalContext.current
    val permissionArray = mutableListOf<String>()

    val isCallLogPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                localContext,
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
                localContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    if (!isCameraPermissionGranted) {
        permissionArray.add(Manifest.permission.CAMERA)
    }

    if (permissionArray.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            localContext as Activity,
            permissionArray.toTypedArray(),
            1
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {

        Spacer(modifier = Modifier.weight(1f))

        // App icon
        Box {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = stringResource(R.string.num_sum_app_logo),
                modifier = Modifier.clip(RoundedCornerShape(20))
            )

            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.num_sum_app_logo),
                modifier = Modifier.clip(RoundedCornerShape(20))
            )
        }

        Spacer(
            modifier = Modifier.weight(0.2f)
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(stringResource(R.string.call_sync_name))
                }
                append(" ")
                append(stringResource(R.string.welcome_description))
            },
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            WelcomeScreen(hiltViewModel(), {})
        }
    }
}