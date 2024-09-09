package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.Manifest
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.firestore.FirebaseFirestoreException
import com.yangian.numsum.core.designsystem.cameraPermissionRequest
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.component.QRCamera
import com.yangian.numsum.core.designsystem.isPermissionGranted
import com.yangian.numsum.core.designsystem.openPermissionSetting
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.onboard.OnBoardViewModel

@Composable
fun ConnectionScreen2(
    onBoardViewModel: OnBoardViewModel,
    appContext: Context,
    modifier: Modifier = Modifier,
    navigateToTemporary: () -> Unit = {},
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            text = "Scan the QR code displayed on your Call Sync App.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        val currentUser = onBoardViewModel.firebaseAuth.currentUser?.uid
        val camera = remember {
            QRCamera()
        }

        var lastScannedBarcode by remember {
            mutableStateOf<String?>(null)
        }

        val rectangleBoundaryColor = MaterialTheme.colorScheme.outline

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            if (context.isPermissionGranted(Manifest.permission.CAMERA)) {
                camera.CameraPreview(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1f)
                        .border(2.dp, rectangleBoundaryColor, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp)),
                    onBarcodeScanned = { barcode ->
                        barcode?.displayValue?.let {
                            lastScannedBarcode = it

                            var receiverId: String? = null
                            if (!lastScannedBarcode.isNullOrBlank()) {
                                receiverId = lastScannedBarcode
                            }

                            receiverId?.let {
                                // UID found in the QR code
                                currentUser?.let {
                                    val db = onBoardViewModel.firebaseFirestore
                                    val documentRef = db.collection("logs").document(receiverId)
                                    db.runTransaction { transaction ->
                                        val snapshot = transaction.get(documentRef)
                                        if (!snapshot.exists()) {
                                            throw FirebaseFirestoreException(
                                                "Document does not exist",
                                                FirebaseFirestoreException.Code.NOT_FOUND
                                            )
                                        }

                                        if (snapshot.getString("sender") != currentUser) {
                                            throw FirebaseFirestoreException(
                                                "Sender ID mismatch",
                                                FirebaseFirestoreException.Code.PERMISSION_DENIED
                                            )
                                        }
                                    }.addOnSuccessListener {
                                        onBoardViewModel.handleSuccessfulScan(
                                            receiverId,
                                            appContext,
                                            navigateToTemporary
                                        )
                                    }.addOnFailureListener {
                                        onBoardViewModel.navigateToPreviousScreen()
                                    }
                                }
                            }
                        }
                    }
                )
            } else {
                CircularProgressIndicator()
                context.cameraPermissionRequest {
                    context.openPermissionSetting()
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}


@Preview
@Composable
private fun ConnectionScreen2Preview() {
    NumSumAppTheme {
        NumSumAppBackground {
            ConnectionScreen2(
                onBoardViewModel = hiltViewModel(),
                appContext = LocalContext.current
            )
        }
    }
}