package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.yangian.numsum.core.designsystem.MultiDevicePreview
import com.yangian.numsum.core.designsystem.component.AppBackground
import com.yangian.numsum.core.designsystem.theme.AppTheme
import com.yangian.numsum.feature.onboard.HandShakeUI
import com.yangian.numsum.feature.onboard.R
import qrcode.QRCode

@Composable
fun CompactPortraitConnection1Screen(
    handShakeString: String,
    getQrCode: (String, Int, Int) -> ImageBitmap,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.weight(10f))

        Text(
            text = stringResource(id = R.string.connection2_desc),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.weight(2f))

        OutlinedCard(
            modifier = Modifier
        ) {
            Image(
                bitmap =
                getQrCode(
                    handShakeString,
                    MaterialTheme.colorScheme.surface.toArgb(),
                    MaterialTheme.colorScheme.onSurface.toArgb()
                ),
                contentDescription = "QR code for connection",
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.padding_medium)
                )
            )
        }

        Spacer(modifier = Modifier.weight(10f))
    }
}

@Composable
fun MediumPortraitConnection1Screen(
    handShakeString: String,
    getQrCode: (String, Int, Int) -> ImageBitmap,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.weight(10f))

        Text(
            text = stringResource(id = R.string.connection2_desc),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.weight(2f))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Image(
                bitmap =
                getQrCode(
                    handShakeString,
                    MaterialTheme.colorScheme.surface.toArgb(),
                    MaterialTheme.colorScheme.onSurface.toArgb()
                ),
                contentDescription = "QR code for connection",
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.padding_medium)
                )
            )
        }

        Spacer(modifier = Modifier.weight(10f))
    }
}

@Composable
fun ExpandedPortraitConnection1Screen(
    handShakeString: String,
    getQrCode: (String, Int, Int) -> ImageBitmap,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.weight(10f))

        Text(
            text = stringResource(id = R.string.connection2_desc),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.6f)
        )

        Spacer(modifier = Modifier.weight(3f))

        OutlinedCard(
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Image(
                bitmap =
                getQrCode(
                    handShakeString,
                    MaterialTheme.colorScheme.surface.toArgb(),
                    MaterialTheme.colorScheme.onSurface.toArgb()
                ),
                contentDescription = "QR code for connection",
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.padding_medium)
                )
            )
        }

        Spacer(modifier = Modifier.weight(10f))
    }
}

@Composable
fun PortraitConnection1Screen(
    handShakeString: String,
    getQrCode: (String, Int, Int) -> ImageBitmap,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> {
            CompactPortraitConnection1Screen(
                handShakeString,
                getQrCode,
                modifier
            )
        }

        WindowWidthSizeClass.MEDIUM -> {
            MediumPortraitConnection1Screen(
                handShakeString,
                getQrCode,
                modifier
            )
        }

        WindowWidthSizeClass.EXPANDED -> {
            ExpandedPortraitConnection1Screen(
                handShakeString,
                getQrCode,
                modifier
            )
        }
    }
}

@Composable
fun CompactLandscapeConnection1Screen(
    handShakeString: String,
    getQrCode: (String, Int, Int) -> ImageBitmap,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(10f))

        OutlinedCard(
            modifier = Modifier.fillMaxHeight(0.8f)
        ) {
            Image(
                bitmap = getQrCode(
                    handShakeString,
                    MaterialTheme.colorScheme.surface.toArgb(),
                    MaterialTheme.colorScheme.onSurface.toArgb()
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }

        Spacer(modifier = Modifier.weight(6f))

        Text(
            text = stringResource(id = R.string.connection2_desc),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.6f)
        )

        Spacer(modifier = Modifier.weight(10f))
    }
}

@Composable
fun MediumLandscapeConnection1Screen(
    handShakeString: String,
    getQrCode: (String, Int, Int) -> ImageBitmap,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(10f))

        OutlinedCard(
            modifier = Modifier.fillMaxHeight(0.6f)
        ) {
            Image(
                bitmap = getQrCode(
                    handShakeString,
                    MaterialTheme.colorScheme.surface.toArgb(),
                    MaterialTheme.colorScheme.onSurface.toArgb()
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }

        Spacer(modifier = Modifier.weight(6f))

        Text(
            text = stringResource(id = R.string.connection2_desc),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.6f)
        )

        Spacer(modifier = Modifier.weight(10f))
    }
}

@Composable
fun ExpandedLandscapeConnection1Screen(
    handShakeString: String,
    getQrCode: (String, Int, Int) -> ImageBitmap,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(10f))

        OutlinedCard(
            modifier = Modifier.fillMaxHeight(0.5f)
        ) {
            Image(
                bitmap = getQrCode(
                    handShakeString,
                    MaterialTheme.colorScheme.surface.toArgb(),
                    MaterialTheme.colorScheme.onSurface.toArgb()
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }

        Spacer(modifier = Modifier.weight(6f))

        Text(
            text = stringResource(id = R.string.connection2_desc),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.4f)
        )

        Spacer(modifier = Modifier.weight(10f))
    }
}

@Composable
fun LandscapeConnection1Screen(
    handShakeString: String,
    getQrCode: (String, Int, Int) -> ImageBitmap,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    when (windowSizeClass.windowHeightSizeClass) {
        WindowHeightSizeClass.COMPACT -> {
            CompactLandscapeConnection1Screen(
                handShakeString,
                getQrCode,
                modifier
            )
        }

        WindowHeightSizeClass.MEDIUM -> {
            MediumLandscapeConnection1Screen(
                handShakeString,
                getQrCode,
                modifier
            )
        }

        WindowHeightSizeClass.EXPANDED -> {
            ExpandedLandscapeConnection1Screen(
                handShakeString,
                getQrCode,
                modifier
            )
        }
    }
}

@Composable
fun Connection1Screen(
    getQRCode: (String, Int, Int) -> ImageBitmap,
    handShakeUIState: HandShakeUI,
    prepareQrCode: () -> Unit,
    turnHandShakeUICold: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenOrientation = LocalContext.current.resources.configuration.orientation

    when (handShakeUIState) {
        is HandShakeUI.Cold -> {
            prepareQrCode()
        }

        is HandShakeUI.Loading -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) { CircularProgressIndicator() }
        }

        is HandShakeUI.Error -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                Text(
                    text = "Something went wrong"
                )

                Spacer(
                    modifier = Modifier.padding(
                        dimensionResource(R.dimen.padding_extra_large)
                    )
                )

                Button(
                    onClick = turnHandShakeUICold,
                ) {
                    Text(
                        text = "Retry"
                    )
                }
            }
        }

        is HandShakeUI.Success -> {
            val handShakeString = handShakeUIState.encryptedHandShakeString

            when (screenOrientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    LandscapeConnection1Screen(
                        handShakeString,
                        getQRCode,
                        modifier
                    )
                }

                else -> {
                    PortraitConnection1Screen(
                        handShakeString,
                        getQRCode,
                        modifier
                    )
                }
            }
        }
    }
}

@MultiDevicePreview
@Composable
private fun Connection1ScreenPreviewPhone() {
    val backgroundColor = MaterialTheme.colorScheme.surface.toArgb()
    val foregroundColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val byteArray: ByteArray = QRCode
        .ofSquares()
        .withBackgroundColor(backgroundColor)
        .withColor(foregroundColor)
        .build(stringResource(R.string.test_user))
        .renderToBytes()

    val imageBitMap = BitmapFactory
        .decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    AppTheme {
        AppBackground {
            Connection1Screen(
                { _, _, _ -> imageBitMap },
                handShakeUIState = HandShakeUI.Success("Test User"),
                prepareQrCode = {},
                turnHandShakeUICold = {},
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}