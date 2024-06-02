package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.onboard.OnBoardViewModel
import com.yangian.numsum.feature.onboard.R
import qrcode.QRCode

@Composable
fun ConnectionScreen1(
    onBoardViewModel: OnBoardViewModel,
    modifier: Modifier = Modifier
) {

    val firebaseAuth = onBoardViewModel.firebaseAuth
    val currentUser = firebaseAuth.currentUser

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(id = R.string.connection2_desc),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.weight(1f))


        val uid: String = currentUser!!.uid

        // generate and display the qr code
        val uidQRCode = QRCode
            .ofSquares()
            .withBackgroundColor(MaterialTheme.colorScheme.secondaryContainer.toArgb())
            .withColor(MaterialTheme.colorScheme.onSecondaryContainer.toArgb())
            .build(uid)

        ElevatedCard {
            Image(
                bitmap = (uidQRCode.render().nativeImage() as Bitmap).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun ConnectionScreen1Preview() {
    NumSumAppTheme {
        NumSumAppBackground {
            ConnectionScreen1(onBoardViewModel = hiltViewModel())
        }
    }
}