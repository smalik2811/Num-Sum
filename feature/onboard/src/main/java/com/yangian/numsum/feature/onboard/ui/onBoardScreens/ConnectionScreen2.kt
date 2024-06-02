package com.yangian.numsum.feature.onboard.ui.onBoardScreens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.moduleinstall.InstallStatusListener
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.feature.onboard.OnBoardViewModel
import com.yangian.numsum.feature.onboard.R

@Composable
fun ConnectionScreen2(
    onBoardViewModel: OnBoardViewModel,
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
        val errorMessage = stringResource(R.string.something_went_wrong)
        var isModuleAvailable by remember {
            mutableStateOf(false)
        }
        var downloadProgress by remember { mutableIntStateOf(0) }

        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
            )
            .enableAutoZoom()
            .build()

        // Check if the module is installed
        val moduleInstallClient = ModuleInstall.getClient(context)

        val scanner = GmsBarcodeScanning.getClient(context)

        class ModuleInstallProgressListener : InstallStatusListener {
            override fun onInstallStatusUpdated(update: ModuleInstallStatusUpdate) {
                // Progress info is only set when modules are in the progress of downloading.
                update.progressInfo?.let {
                    val progress = (it.bytesDownloaded * 100 / it.totalBytesToDownload).toInt()
                    // Set the progress for the progress bar.
                    downloadProgress = progress
                }

                if (isTerminateState(update.installState)) {
                    moduleInstallClient.unregisterListener(this)
                }
            }

            fun isTerminateState(@ModuleInstallStatusUpdate.InstallState state: Int): Boolean {
                return state == ModuleInstallStatusUpdate.InstallState.STATE_CANCELED || state == ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED || state == ModuleInstallStatusUpdate.InstallState.STATE_FAILED
            }
        }

        val listener = ModuleInstallProgressListener()

        moduleInstallClient
            .areModulesAvailable(scanner)
            .addOnSuccessListener {
                if (it.areModulesAvailable()) {
                    // Modules are present on the device
                    isModuleAvailable = true

                } else {
                    // Modules are not present on the device
                    val gmsBarCodeScannerInstallRequest = ModuleInstallRequest
                        .newBuilder()
                        .addApi(scanner)
                        .setListener(listener)
                        .build()

                    moduleInstallClient
                        .installModules(gmsBarCodeScannerInstallRequest)
                        .addOnSuccessListener { task ->
                            if (task.areModulesAlreadyInstalled()) {
                                // Modules are available
                                isModuleAvailable = true
                            }
                        }
                        .addOnFailureListener {
                            // Could not install Modules
                            onBoardViewModel.navigateToPreviousScreen()
                        }

                }
            }
            .addOnFailureListener {
                // Handle failure
                onBoardViewModel.navigateToPreviousScreen()
            }

        if (isModuleAvailable) {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val receiverId: String? = barcode.rawValue
                    val currentUser = onBoardViewModel.firebaseAuth.currentUser?.uid ?: ""

                    if (receiverId != null) {

                        val db = onBoardViewModel.firebaseFirestore
                        val documentRef = db.collection("call-logs").document(receiverId)


                        documentRef.get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    if (document.getString("sender_id") == currentUser) {
                                        onBoardViewModel.updateReceiverId(receiverId)
                                        onBoardViewModel.updateOnBoardingCompleted(true)
                                        navigateToTemporary()
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
                    // Task failed with an exception
                    println(e.message)
                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()

                    onBoardViewModel.navigateToPreviousScreen()
                }
        } else {
            CircularProgressIndicator(progress = { downloadProgress / 100f })
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun ConnectionScreen2Preview() {
    NumSumAppTheme {
        NumSumAppBackground {
            ConnectionScreen2(onBoardViewModel = hiltViewModel())
        }
    }
}