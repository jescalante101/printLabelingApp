import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@Composable
fun BarcodeScannerScreen(
    onBarcodeScanned: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(Modifier.fillMaxSize()) {
        if (!hasCameraPermission) {
            Text(
                "Se necesita permiso de cámara para escanear.",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            val lifecycleOwner = LocalContext.current as? LifecycleOwner
            if (lifecycleOwner != null) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = androidx.camera.core.Preview.Builder().build()
                            preview.setSurfaceProvider(previewView.surfaceProvider)

                            val barcodeScanner = BarcodeScanning.getClient()
                            val analysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                            analysis.setAnalyzer(
                                ContextCompat.getMainExecutor(ctx),
                                { imageProxy ->
                                    processImageProxy(
                                        imageProxy,
                                        barcodeScanner
                                    ) { barcodeValue ->
                                        if (barcodeValue != null) {
                                            onBarcodeScanned(barcodeValue)
                                        }
                                    }
                                }
                            )

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    preview,
                                    analysis
                                )
                            } catch (e: Exception) {
                                Log.e("BarcodeScanner", "Error al iniciar la cámara", e)
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                }
            } else {
                Text("No hay LifecycleOwner disponible", Modifier.align(Alignment.Center))
            }
        }
    }
}

// Función utilitaria
@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    scanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    onBarcodeFound: (String?) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                val code = barcodes.firstOrNull()?.rawValue
                onBarcodeFound(code)
            }
            .addOnFailureListener { onBarcodeFound(null) }
            .addOnCompleteListener { imageProxy.close() }
    } else {
        imageProxy.close()
    }
}
