package com.example.fibra_labeling.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class BarcodeViewModel(): ViewModel() {
    private val _barcode = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val barcode: SharedFlow<String> = _barcode

    fun onBarcodeScanned(code: String) {
        Log.d("BarcodeViewModel", "onBarcodeScanned: $code")
        _barcode.tryEmit(code)
    }
}