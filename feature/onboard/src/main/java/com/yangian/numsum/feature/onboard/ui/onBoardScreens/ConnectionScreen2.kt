package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
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

        Spacer(modifier = Modifier.weight(1f))

        val context = LocalContext.current
        val isOnboardingCompleted =
            onBoardViewModel.isOnboardingCompleted.collectAsState(initial = false)
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
            )
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(context, options)


        // Check if the module is installed
        val moduleInstallClient = ModuleInstall.getClient(context)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(scanner)
            .build()

        moduleInstallClient
            .installModules(moduleInstallRequest)
            .addOnSuccessListener {
                if (it.areModulesAlreadyInstalled()) {
                    scanner.startScan()
                        .addOnSuccessListener { barcode ->
                            val receiverId: String? = barcode.rawValue
                            val currentUser = onBoardViewModel.firebaseAuth.currentUser?.uid ?: ""

                            if (receiverId != null) {

                                val db = onBoardViewModel.firebaseFirestore
                                val documentRef = db.collection("logs").document(receiverId)


                                documentRef.get()
                                    .addOnSuccessListener { document ->
                                        if (document.exists()) {
                                            if (document.getString("sender") == currentUser) {
                                                onBoardViewModel.updateReceiverId(receiverId)
                                                onBoardViewModel.registerLogsUploadWorkRequest(
                                                    context = appContext
                                                )
                                                onBoardViewModel.updateOnBoardingCompleted(true)
                                                if (!isOnboardingCompleted.value) {
                                                    navigateToTemporary()
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
//                                errorMessage,
                                                    "Sender Id did not match",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                onBoardViewModel.navigateToPreviousScreen()
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
//                                errorMessage,
                                                "Document not exist",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            onBoardViewModel.navigateToPreviousScreen()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
//                            errorMessage,
                                            it.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                        onBoardViewModel.navigateToPreviousScreen()
                                    }
                            }
                        }
                        .addOnCanceledListener {
                            onBoardViewModel.navigateToPreviousScreen()
                        }
                        .addOnFailureListener { e ->
                            // Task failed 44with an exception
                            println(e.message)
                            Toast.makeText(
                                context,
                                e.message,
                                Toast.LENGTH_LONG
                            ).show()

                            onBoardViewModel.navigateToPreviousScreen()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
//                                errorMessage,
                    it.message,
                    Toast.LENGTH_LONG
                ).show()
                onBoardViewModel.navigateToPreviousScreen()
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