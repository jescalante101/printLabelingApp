package com.example.fibra_labeling.fibraprint.ui.screen.etiqueta.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

fun generarCodigoBarras(
    data: String,
    width: Int = 600,
    height: Int = 300
): Bitmap {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(
        data,
        BarcodeFormat.CODE_128, // Puedes cambiar el tipo de código de barras aquí
        width,
        height
    )

    val bmp = createBitmap(width, height)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp[x, y] =
                if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
        }
    }
    return bmp
}