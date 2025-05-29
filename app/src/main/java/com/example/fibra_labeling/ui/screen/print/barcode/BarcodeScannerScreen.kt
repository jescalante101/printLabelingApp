import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
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
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun BarcodeScannerScreen(
    onBack: () -> Unit,
    navController: NavController
) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    // Request permission when the composable enters the composition
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    var barcodeProcessed by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        if (!hasCameraPermission) {
            Text(
                "Se necesita permiso de cámara para escanear.",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            val context = LocalContext.current
            val lifecycleOwner = context as? LifecycleOwner

            // Executor para el análisis de imagen (es buena práctica usar un background thread)
            val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

            // Limpiar el executor cuando el composable sale de la composición
            DisposableEffect(Unit) {
                onDispose {
                    cameraExecutor.shutdown()
                }
            }

            if (lifecycleOwner != null) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build()
                            preview.setSurfaceProvider(previewView.surfaceProvider)

                            val barcodeScannerClient = BarcodeScanning.getClient() // Renombrado para evitar confusión
                            val analysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            analysis.setAnalyzer(cameraExecutor) { imageProxy -> // Usar el executor
                                if (!barcodeProcessed){
                                    processImageProxy(
                                        imageProxy,
                                        barcodeScannerClient // Pasamos el cliente real
                                    ) { barcodeValue ->
                                        if (barcodeValue != null) {
                                            Log.e("BarcodeScanner", "Barcode found: $barcodeValue")
                                            barcodeProcessed = true // Marcar como procesado
                                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                                "barcode_scan_result", barcodeValue
                                            )
                                            navController.popBackStack()
                                            cameraProvider.unbind(analysis)
                                        }
                                    }
                                }
                            }

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    preview,
                                    analysis
                                )
                            } catch (e: Exception) {
                                Log.e("BarcodeScanner", "Error al iniciar la cámara: ${e.message}", e)
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
    scanner: BarcodeScanner,
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


