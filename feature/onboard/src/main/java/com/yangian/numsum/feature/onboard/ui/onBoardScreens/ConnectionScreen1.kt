package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.onboard.HandShakeUI
import com.yangian.numsum.feature.onboard.R
import qrcode.QRCode

@Composable
fun ConnectionScreen1(
    handShakeUIState: HandShakeUI,
    prepareQrCode: (backgroundColor: Int, foregroundColor: Int) -> Unit,
    turnHandShakeUICold: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
    ) {
        when (handShakeUIState) {
            is HandShakeUI.Cold -> {
                prepareQrCode(
                    MaterialTheme.colorScheme.surface.toArgb(),
                    MaterialTheme.colorScheme.onSurface.toArgb()
                )
            }

            is HandShakeUI.Loading -> {
                CircularProgressIndicator()
            }

            is HandShakeUI.Error -> {
                Text(
                    text = "Something went wrong"
                )

                Spacer(
                    modifier = Modifier.padding(
                        dimensionResource(R.dimen.padding_medium)
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

            is HandShakeUI.Success -> {

                Spacer(modifier = Modifier.weight(0.5f))

                Text(
                    text = stringResource(id = R.string.connection2_desc),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                OutlinedCard {
                    Image(
                        bitmap = handShakeUIState.qrCode,
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
private fun ConnectionScreen1Preview() {
    val backgroundColor = MaterialTheme.colorScheme.surface.toArgb()
    val foregroundColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val byteArray: ByteArray = QRCode
        .ofSquares()
        .withBackgroundColor(backgroundColor)
        .withColor(foregroundColor)
        .build("Test User")
        .renderToBytes()

    val imageBitMap = BitmapFactory
        .decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()

    NumSumAppTheme {
        NumSumAppBackground {
            ConnectionScreen1(
                handShakeUIState = HandShakeUI.Success(imageBitMap),
                prepareQrCode = { _, _ -> },
                turnHandShakeUICold = {}
            )
        }
    }
}