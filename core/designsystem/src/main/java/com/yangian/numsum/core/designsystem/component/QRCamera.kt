package com.yangian.numsum.core.designsystem.component

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.yangian.numsum.core.designsystem.R
import com.yangian.numsum.core.designsystem.theme.AppTheme
import java.util.concurrent.Executors

class QRCamera {
    private var camera: Camera? = null

    @Composable
    fun CameraPreviewView(
        modifier: Modifier = Modifier,
        onBarcodeScanned: (Barcode?) -> Unit,
        isPreviewing: Boolean = false
    ) {
        if (isPreviewing) {
            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = "Image from the internet",
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize(),
            )
        } else {


            val lifecycleOwner = LocalLifecycleOwner.current

            val imageCapture = remember {
                ImageCapture
                    .Builder()
                    .build()
            }

            AndroidView(
                modifier = modifier,
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams =
                            LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        scaleType = PreviewView.ScaleType.FILL_START

                        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                        cameraProviderFuture.addListener(
                            {
                                startCamera(
                                    context = context,
                                    previewView = this,
                                    imageCapture = imageCapture,
                                    lifecycleOwner = lifecycleOwner,
                                    onBarcodeScanned = onBarcodeScanned
                                )
                            },
                            ContextCompat.getMainExecutor(context)
                        )
                    }
                }
            )
        }
    }

    private fun startCamera(
        context: Context,
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        imageCapture: ImageCapture,
        onBarcodeScanned: (Barcode?) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)

                    }

                val executor = Executors.newSingleThreadExecutor()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.TYPE_TEXT
                    ).build()

                val scanner = BarcodeScanning.getClient(options)

                imageAnalysis.setAnalyzer(executor) { imageProxy ->
                    processImageProxy(
                        barcodeScanner = scanner,
                        imageProxy = imageProxy,
                        onSuccess = onBarcodeScanned
                    )
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis
                    )
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", exc)
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,
        onSuccess: (Barcode?) -> Unit
    ) {
        imageProxy.image?.let { image ->
            val inputImage =
                InputImage.fromMediaImage(
                    image,
                    imageProxy.imageInfo.rotationDegrees
                )

            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    val barcode = barcodeList.getOrNull(0)
                    onSuccess(barcode)
                }
                .addOnFailureListener {
                    // This failure will happen if the barcode scanning model
                    // fails to download from Google Play Services
                    Log.e("CameraPreview", it.message.orEmpty())
                }.addOnCompleteListener {
                    // When the image is from CameraX analysis use case, must
                    // call image.close() on received images when finished
                    // using them. Otherwise, new images may not be received
                    // or the camera may stall.
                    imageProxy.image?.close()
                    imageProxy.close()
                }
        }
    }

    @androidx.compose.ui.tooling.preview.Preview
    @Composable
    private fun CameraPreviewView_Preview() {
        AppTheme {
            AppBackground {
                CameraPreviewView(
                    isPreviewing = true,
                    onBarcodeScanned = {},
                )
            }
        }
    }
}