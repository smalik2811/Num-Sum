package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.google.mlkit.vision.barcode.common.Barcode
import com.yangian.numsum.core.designsystem.MultiDevicePreview
import com.yangian.numsum.core.designsystem.component.AppBackground
import com.yangian.numsum.core.designsystem.component.CustomAlertDialog
import com.yangian.numsum.core.designsystem.component.QRCamera
import com.yangian.numsum.core.designsystem.icon.QrCodeScannerIcon
import com.yangian.numsum.core.designsystem.isPermissionDeniedPermanently
import com.yangian.numsum.core.designsystem.isPermissionGranted
import com.yangian.numsum.core.designsystem.openPermissionSetting
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.core.designsystem.theme.extendedDark
import com.yangian.numsum.core.designsystem.theme.extendedLight
import com.yangian.numsum.feature.onboard.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CompactPortraitConnection2Screen(
    validateReceiver: (
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit,
    ) -> Unit,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviewing: Boolean = false,
) {
    val context = LocalContext.current
    var scannedValue by remember { mutableStateOf<String?>(null) }
    var isCameraPermissionGranted by remember {
        mutableStateOf(context.isPermissionGranted(Manifest.permission.CAMERA))
    }
    val isCallLogPermissionGranted by remember {
        mutableStateOf(context.isPermissionGranted(Manifest.permission.READ_CALL_LOG))
    }
    if (!isCallLogPermissionGranted) {

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_CALL_LOG),
            1
        )
    }

    var showRationale by remember { mutableStateOf(false) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isCameraPermissionGranted = isGranted
            showRationale =
                !isGranted || context.isPermissionDeniedPermanently(Manifest.permission.CAMERA)
        }
    )

    var hasScanned by remember { mutableStateOf(false) }
    val onBarCodeScanned: (Barcode?) -> Unit = { barcode ->
        barcode?.rawValue?.let { value ->
            if (!hasScanned) {
                scannedValue = value
                hasScanned = true
            }
        }
    }

    val rectangleBoundaryColor = if (isSystemInDarkTheme()) {
        extendedDark.success.color
    } else {
        extendedLight.success.color
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1500)
            isCameraPermissionGranted = context.isPermissionGranted(Manifest.permission.CAMERA)
        }
    }


    if (!hasScanned) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {

            Spacer(modifier = Modifier.weight(10f))

            Text(
                text = "Point the camera towards Call Sync app and scan the QR code",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCameraPermissionGranted -> {
                        QRCamera().CameraPreviewView(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .border(
                                    dimensionResource(R.dimen.padding_tiny),
                                    rectangleBoundaryColor,
                                    RoundedCornerShape(
                                        dimensionResource(R.dimen.corner_radius_medium)
                                    )
                                )
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))),
                            onBarcodeScanned = onBarCodeScanned,
                            isPreviewing = isPreviewing
                        )
                    }

                    showRationale -> {
                        val annotatedDialogMessage = buildAnnotatedString {
                            append(stringResource(R.string.camera_permission_explanation_part_1))
                            append(stringResource(R.string.space_string))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.app_name))
                            }
                            append(stringResource(R.string.space_string))
                            append(stringResource(R.string.camer_permisssion_explanation_part_2))
                        }

                        CustomAlertDialog(
                            onPositiveButtonClick = { context.openPermissionSetting() },
                            onNegativeButtonClick = { navigateToPreviousScreen() },
                            onDismissRequest = { navigateToPreviousScreen() },
                            dialogTitle = stringResource(R.string.camera_permission_request_title),
                            dialogText = annotatedDialogMessage,
                            positiveButtonText = stringResource(R.string.allow_camera),
                            negativeButtonText = stringResource(R.string.cancel),
                            iconContentDescriptionText = stringResource(R.string.qr_code_scanner),
                            modifier = Modifier,
                            icon = QrCodeScannerIcon
                        )
                    }

                    else -> {
                        LaunchedEffect(Unit) {
                            cameraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(10f))
        }

    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { CircularProgressIndicator() }

        scannedValue?.let {
            // UID found in the QR code

            LaunchedEffect(Unit) {
                launch(Dispatchers.IO) {
                    validateReceiver(
                        scannedValue!!,
                        context,
                        navigateToHome
                    )
                }
            }
        }
    }
}

@Composable
fun MediumPortraitConnection2Screen(
    validateReceiver: (
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit,
    ) -> Unit,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviewing: Boolean = false,
) {
    val context = LocalContext.current
    var scannedValue by remember { mutableStateOf<String?>(null) }
    var isCameraPermissionGranted by remember {
        mutableStateOf(context.isPermissionGranted(Manifest.permission.CAMERA))
    }
    val isCallLogPermissionGranted by remember {
        mutableStateOf(context.isPermissionGranted(Manifest.permission.READ_CALL_LOG))
    }
    if (!isCallLogPermissionGranted) {

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_CALL_LOG),
            1
        )
    }

    var showRationale by remember { mutableStateOf(false) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isCameraPermissionGranted = isGranted
            showRationale =
                !isGranted || context.isPermissionDeniedPermanently(Manifest.permission.CAMERA)
        }
    )

    var hasScanned by remember { mutableStateOf(false) }
    val onBarCodeScanned: (Barcode?) -> Unit = { barcode ->
        barcode?.rawValue?.let { value ->
            if (!hasScanned) {
                scannedValue = value
                hasScanned = true
            }
        }
    }

    val rectangleBoundaryColor = if (isSystemInDarkTheme()) {
        extendedDark.success.color
    } else {
        extendedLight.success.color
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1500)
            isCameraPermissionGranted = context.isPermissionGranted(Manifest.permission.CAMERA)
        }
    }


    if (!hasScanned) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {

            Spacer(modifier = Modifier.weight(10f))

            Text(
                text = "Point the camera towards Call Sync app and scan the QR code",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCameraPermissionGranted -> {
                        QRCamera().CameraPreviewView(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .border(
                                    dimensionResource(R.dimen.padding_small),
                                    rectangleBoundaryColor,
                                    RoundedCornerShape(
                                        dimensionResource(R.dimen.corner_radius_medium)
                                    )
                                )
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))),
                            onBarcodeScanned = onBarCodeScanned,
                            isPreviewing = isPreviewing
                        )
                    }

                    showRationale -> {
                        val annotatedDialogMessage = buildAnnotatedString {
                            append(stringResource(R.string.camera_permission_explanation_part_1))
                            append(stringResource(R.string.space_string))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.app_name))
                            }
                            append(stringResource(R.string.space_string))
                            append(stringResource(R.string.camer_permisssion_explanation_part_2))
                        }

                        CustomAlertDialog(
                            onPositiveButtonClick = { context.openPermissionSetting() },
                            onNegativeButtonClick = { navigateToPreviousScreen() },
                            onDismissRequest = { navigateToPreviousScreen() },
                            dialogTitle = stringResource(R.string.camera_permission_request_title),
                            dialogText = annotatedDialogMessage,
                            positiveButtonText = stringResource(R.string.allow_camera),
                            negativeButtonText = stringResource(R.string.cancel),
                            iconContentDescriptionText = stringResource(R.string.qr_code_scanner),
                            modifier = Modifier,
                            icon = QrCodeScannerIcon
                        )
                    }

                    else -> {
                        LaunchedEffect(Unit) {
                            cameraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(10f))
        }

    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { CircularProgressIndicator() }

        scannedValue?.let {
            // UID found in the QR code

            LaunchedEffect(Unit) {
                launch(Dispatchers.IO) {
                    validateReceiver(
                        scannedValue!!,
                        context,
                        navigateToHome
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandedPortraitConnection2Screen(
    validateReceiver: (
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit,
    ) -> Unit,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviewing: Boolean = false,
) {
    val context = LocalContext.current
    var scannedValue by remember { mutableStateOf<String?>(null) }
    var isCameraPermissionGranted by remember {
        mutableStateOf(context.isPermissionGranted(Manifest.permission.CAMERA))
    }
    val isCallLogPermissionGranted by remember {
        mutableStateOf(context.isPermissionGranted(Manifest.permission.READ_CALL_LOG))
    }
    if (!isCallLogPermissionGranted) {

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_CALL_LOG),
            1
        )
    }

    var showRationale by remember { mutableStateOf(false) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isCameraPermissionGranted = isGranted
            showRationale =
                !isGranted || context.isPermissionDeniedPermanently(Manifest.permission.CAMERA)
        }
    )

    var hasScanned by remember { mutableStateOf(false) }
    val onBarCodeScanned: (Barcode?) -> Unit = { barcode ->
        barcode?.rawValue?.let { value ->
            if (!hasScanned) {
                scannedValue = value
                hasScanned = true
            }
        }
    }

    val rectangleBoundaryColor = if (isSystemInDarkTheme()) {
        extendedDark.success.color
    } else {
        extendedLight.success.color
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1500)
            isCameraPermissionGranted = context.isPermissionGranted(Manifest.permission.CAMERA)
        }
    }


    if (!hasScanned) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {

            Spacer(modifier = Modifier.weight(10f))

            Text(
                text = "Point the camera towards Call Sync app and scan the QR code",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(2f))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCameraPermissionGranted -> {
                        QRCamera().CameraPreviewView(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .border(
                                    dimensionResource(R.dimen.padding_small),
                                    rectangleBoundaryColor,
                                    RoundedCornerShape(
                                        dimensionResource(R.dimen.corner_radius_medium)
                                    )
                                )
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))),
                            onBarcodeScanned = onBarCodeScanned,
                            isPreviewing = isPreviewing
                        )
                    }

                    showRationale -> {
                        val annotatedDialogMessage = buildAnnotatedString {
                            append(stringResource(R.string.camera_permission_explanation_part_1))
                            append(stringResource(R.string.space_string))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.app_name))
                            }
                            append(stringResource(R.string.space_string))
                            append(stringResource(R.string.camer_permisssion_explanation_part_2))
                        }

                        CustomAlertDialog(
                            onPositiveButtonClick = { context.openPermissionSetting() },
                            onNegativeButtonClick = { navigateToPreviousScreen() },
                            onDismissRequest = { navigateToPreviousScreen() },
                            dialogTitle = stringResource(R.string.camera_permission_request_title),
                            dialogText = annotatedDialogMessage,
                            positiveButtonText = stringResource(R.string.allow_camera),
                            negativeButtonText = stringResource(R.string.cancel),
                            iconContentDescriptionText = stringResource(R.string.qr_code_scanner),
                            modifier = Modifier,
                            icon = QrCodeScannerIcon
                        )
                    }

                    else -> {
                        LaunchedEffect(Unit) {
                            cameraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(10f))
        }

    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { CircularProgressIndicator() }

        scannedValue?.let {
            // UID found in the QR code

            LaunchedEffect(Unit) {
                launch(Dispatchers.IO) {
                    validateReceiver(
                        scannedValue!!,
                        context,
                        navigateToHome
                    )
                }
            }
        }
    }
}

@Composable
fun PortraitConnection2Screen(
    validateReceiver: (
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit,
    ) -> Unit,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviewing: Boolean = false
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> {
            CompactPortraitConnection2Screen(
                validateReceiver,
                navigateToHome,
                navigateToPreviousScreen,
                modifier,
                isPreviewing
            )
        }

        WindowWidthSizeClass.MEDIUM -> {
            MediumPortraitConnection2Screen(
                validateReceiver,
                navigateToHome,
                navigateToPreviousScreen,
                modifier,
                isPreviewing
            )
        }

        WindowWidthSizeClass.EXPANDED -> {
            ExpandedPortraitConnection2Screen(
                validateReceiver,
                navigateToHome,
                navigateToPreviousScreen,
                modifier,
                isPreviewing
            )
        }
    }
}

@Composable
fun CompactLandscapeConnection2Screen(
    validateReceiver: (
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit,
    ) -> Unit,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviewing: Boolean = false,
) {
    val context = LocalContext.current
    var scannedValue by remember { mutableStateOf<String?>(null) }
    var isCameraPermissionGranted by remember {
        mutableStateOf(context.isPermissionGranted(Manifest.permission.CAMERA))
    }
    var showRationale by remember { mutableStateOf(false) }
    var hasScanned by remember { mutableStateOf(false) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isCameraPermissionGranted = isGranted
            showRationale =
                !isGranted || context.isPermissionDeniedPermanently(Manifest.permission.CAMERA)
        }
    )
    val onBarCodeScanned: (Barcode?) -> Unit = { barcode ->
        barcode?.rawValue?.let { value ->
            if (!hasScanned) {
                scannedValue = value
                hasScanned = true
            }
        }
    }

    val rectangleBoundaryColor = if (isSystemInDarkTheme()) {
        extendedDark.success.color
    } else {
        extendedLight.success.color
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1500) // Check every second
            isCameraPermissionGranted = context.isPermissionGranted(Manifest.permission.CAMERA)
        }
    }

    if (!hasScanned) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.weight(10f))
            Box(
                modifier = Modifier
                    .aspectRatio(1f, true),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCameraPermissionGranted -> {
                        QRCamera().CameraPreviewView(
                            modifier = Modifier
                                .border(
                                    dimensionResource(R.dimen.padding_tiny),
                                    rectangleBoundaryColor,
                                    RoundedCornerShape(
                                        dimensionResource(R.dimen.corner_radius_medium)
                                    )
                                )
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))),
                            onBarcodeScanned = onBarCodeScanned,
                            isPreviewing = isPreviewing
                        )
                    }

                    showRationale -> {
                        val annotatedDialogMessage = buildAnnotatedString {
                            append(stringResource(R.string.camera_permission_explanation_part_1))
                            append(stringResource(R.string.space_string))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.app_name))
                            }
                            append(stringResource(R.string.space_string))
                            append(stringResource(R.string.camer_permisssion_explanation_part_2))
                        }

                        CustomAlertDialog(
                            onPositiveButtonClick = { context.openPermissionSetting() },
                            onNegativeButtonClick = { navigateToPreviousScreen() },
                            onDismissRequest = { navigateToPreviousScreen() },
                            dialogTitle = stringResource(R.string.camera_permission_request_title),
                            dialogText = annotatedDialogMessage,
                            positiveButtonText = stringResource(R.string.allow_camera),
                            negativeButtonText = stringResource(R.string.cancel),
                            iconContentDescriptionText = stringResource(R.string.qr_code_scanner),
                            modifier = Modifier,
                            icon = QrCodeScannerIcon
                        )
                    }

                    else -> {
                        LaunchedEffect(Unit) {
                            cameraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(2f))

            Text(
                text = "Point the camera towards Call Sync app and scan the QR code",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.weight(10f))
        }

    } else {
        CircularProgressIndicator()
        scannedValue?.let {
            // UID found in the QR code
            LaunchedEffect(Unit) {
                launch(Dispatchers.IO) {
                    validateReceiver(
                        scannedValue!!,
                        context,
                        navigateToHome
                    )
                }
            }
        }
    }
}

@Composable
fun MediumLandscapeConnection2Screen(
    validateReceiver: (
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit,
    ) -> Unit,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviewing: Boolean = false,
) {
    val context = LocalContext.current
    var scannedValue by remember { mutableStateOf<String?>(null) }
    var isCameraPermissionGranted by remember {
        mutableStateOf(context.isPermissionGranted(Manifest.permission.CAMERA))
    }
    var showRationale by remember { mutableStateOf(false) }
    var hasScanned by remember { mutableStateOf(false) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isCameraPermissionGranted = isGranted
            showRationale =
                !isGranted || context.isPermissionDeniedPermanently(Manifest.permission.CAMERA)
        }
    )
    val onBarCodeScanned: (Barcode?) -> Unit = { barcode ->
        barcode?.rawValue?.let { value ->
            if (!hasScanned) {
                scannedValue = value
                hasScanned = true
            }
        }
    }

    val rectangleBoundaryColor = if (isSystemInDarkTheme()) {
        extendedDark.success.color
    } else {
        extendedLight.success.color
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1500) // Check every second
            isCameraPermissionGranted = context.isPermissionGranted(Manifest.permission.CAMERA)
        }
    }

    if (!hasScanned) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.weight(6f))
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .aspectRatio(1f, true),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCameraPermissionGranted -> {
                        QRCamera().CameraPreviewView(
                            modifier = Modifier
                                .border(
                                    dimensionResource(R.dimen.padding_tiny),
                                    rectangleBoundaryColor,
                                    RoundedCornerShape(
                                        dimensionResource(R.dimen.corner_radius_medium)
                                    )
                                )
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))),
                            onBarcodeScanned = onBarCodeScanned,
                            isPreviewing = isPreviewing
                        )
                    }

                    showRationale -> {
                        val annotatedDialogMessage = buildAnnotatedString {
                            append(stringResource(R.string.camera_permission_explanation_part_1))
                            append(stringResource(R.string.space_string))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.app_name))
                            }
                            append(stringResource(R.string.space_string))
                            append(stringResource(R.string.camer_permisssion_explanation_part_2))
                        }

                        CustomAlertDialog(
                            onPositiveButtonClick = { context.openPermissionSetting() },
                            onNegativeButtonClick = { navigateToPreviousScreen() },
                            onDismissRequest = { navigateToPreviousScreen() },
                            dialogTitle = stringResource(R.string.camera_permission_request_title),
                            dialogText = annotatedDialogMessage,
                            positiveButtonText = stringResource(R.string.allow_camera),
                            negativeButtonText = stringResource(R.string.cancel),
                            iconContentDescriptionText = stringResource(R.string.qr_code_scanner),
                            modifier = Modifier,
                            icon = QrCodeScannerIcon
                        )
                    }

                    else -> {
                        LaunchedEffect(Unit) {
                            cameraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(4f))

            Text(
                text = "Point the camera towards Call Sync app and scan the QR code",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.6f)
            )

            Spacer(modifier = Modifier.weight(10f))
        }

    } else {
        CircularProgressIndicator()
        scannedValue?.let {
            // UID found in the QR code
            LaunchedEffect(Unit) {
                launch(Dispatchers.IO) {
                    validateReceiver(
                        scannedValue!!,
                        context,
                        navigateToHome
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandedLandscapeConnection2Screen(
    validateReceiver: (
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit,
    ) -> Unit,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviewing: Boolean = false,
) {
    val context = LocalContext.current
    var scannedValue by remember { mutableStateOf<String?>(null) }
    var isCameraPermissionGranted by remember {
        mutableStateOf(context.isPermissionGranted(Manifest.permission.CAMERA))
    }
    var showRationale by remember { mutableStateOf(false) }
    var hasScanned by remember { mutableStateOf(false) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isCameraPermissionGranted = isGranted
            showRationale =
                !isGranted || context.isPermissionDeniedPermanently(Manifest.permission.CAMERA)
        }
    )
    val onBarCodeScanned: (Barcode?) -> Unit = { barcode ->
        barcode?.rawValue?.let { value ->
            if (!hasScanned) {
                scannedValue = value
                hasScanned = true
            }
        }
    }

    val rectangleBoundaryColor = if (isSystemInDarkTheme()) {
        extendedDark.success.color
    } else {
        extendedLight.success.color
    }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1500) // Check every second
            isCameraPermissionGranted = context.isPermissionGranted(Manifest.permission.CAMERA)
        }
    }

    if (!hasScanned) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.weight(8f))
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .aspectRatio(1f, true),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isCameraPermissionGranted -> {
                        QRCamera().CameraPreviewView(
                            modifier = Modifier
                                .border(
                                    dimensionResource(R.dimen.padding_tiny),
                                    rectangleBoundaryColor,
                                    RoundedCornerShape(
                                        dimensionResource(R.dimen.corner_radius_medium)
                                    )
                                )
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))),
                            onBarcodeScanned = onBarCodeScanned,
                            isPreviewing = isPreviewing
                        )
                    }

                    showRationale -> {
                        val annotatedDialogMessage = buildAnnotatedString {
                            append(stringResource(R.string.camera_permission_explanation_part_1))
                            append(stringResource(R.string.space_string))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.app_name))
                            }
                            append(stringResource(R.string.space_string))
                            append(stringResource(R.string.camer_permisssion_explanation_part_2))
                        }

                        CustomAlertDialog(
                            onPositiveButtonClick = { context.openPermissionSetting() },
                            onNegativeButtonClick = { navigateToPreviousScreen() },
                            onDismissRequest = { navigateToPreviousScreen() },
                            dialogTitle = stringResource(R.string.camera_permission_request_title),
                            dialogText = annotatedDialogMessage,
                            positiveButtonText = stringResource(R.string.allow_camera),
                            negativeButtonText = stringResource(R.string.cancel),
                            iconContentDescriptionText = stringResource(R.string.qr_code_scanner),
                            modifier = Modifier,
                            icon = QrCodeScannerIcon
                        )
                    }

                    else -> {
                        LaunchedEffect(Unit) {
                            cameraLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(6f))

            Text(
                text = "Point the camera towards Call Sync app and scan the QR code",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.4f)
            )

            Spacer(modifier = Modifier.weight(8f))
        }

    } else {
        CircularProgressIndicator()
        scannedValue?.let {
            // UID found in the QR code
            LaunchedEffect(Unit) {
                launch(Dispatchers.IO) {
                    validateReceiver(
                        scannedValue!!,
                        context,
                        navigateToHome
                    )
                }
            }
        }
    }
}

@Composable
fun LandscapeConnection2Screen(
    validateReceiver: (
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit,
    ) -> Unit,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviewing: Boolean = false
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    when (windowSizeClass.windowHeightSizeClass) {
        WindowHeightSizeClass.COMPACT -> {
            CompactLandscapeConnection2Screen(
                validateReceiver,
                navigateToHome,
                navigateToPreviousScreen,
                modifier,
                isPreviewing
            )
        }

        WindowHeightSizeClass.MEDIUM -> {
            MediumLandscapeConnection2Screen(
                validateReceiver,
                navigateToHome,
                navigateToPreviousScreen,
                modifier,
                isPreviewing
            )
        }

        WindowHeightSizeClass.EXPANDED -> {
            ExpandedLandscapeConnection2Screen(
                validateReceiver,
                navigateToHome,
                navigateToPreviousScreen,
                modifier,
                isPreviewing
            )
        }
    }
}

@Composable
fun Connection2Screen(
    validateReceiver: (
        scannedReceiverId: String,
        context: Context,
        navigateToHome: () -> Unit,
    ) -> Unit,
    navigateToHome: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier,
    isPreviewing: Boolean = false,
) {
    val screenOrientation = LocalContext.current.resources.configuration.orientation

    when (screenOrientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeConnection2Screen(
                validateReceiver,
                navigateToHome,
                navigateToPreviousScreen,
                modifier,
                isPreviewing,
            )
        }

        else -> {
            PortraitConnection2Screen(
                validateReceiver,
                navigateToHome,
                navigateToPreviousScreen,
                modifier,
                isPreviewing,
            )
        }
    }
}

@MultiDevicePreview
@Composable
private fun Connection2ScreenPreviewPhone() {
    AppTheme {
        AppBackground {
            Connection2Screen(
                { _, _, _ -> },
                {},
                {},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                        isPreviewing = true
            )
        }
    }
}