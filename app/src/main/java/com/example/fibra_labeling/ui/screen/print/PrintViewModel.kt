package com.example.fibra_labeling.ui.screen.print

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.repository.PesajeRespository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

const val BARCODE_SCAN_RESULT_KEY = "barcode_scan_result"
class PrintViewModel(
    private val pesajeRespository: PesajeRespository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    // Estado de resultado de la consulta
    private val _pesajeResult = MutableStateFlow<Result<ImobPasaje>>(
        Result.success(ImobPasaje(
            itemCode = "",
            name = "",
            proveedor = "",
            lote = "",
            almacen = "",
            motivo = "",
            ubicacion = "",
            piso = "",
            metroLineal = "",
            equivalente = "",
            peso = 0.0,
            codeBar = "",
            userCreate = "",
            createDate = "",
            status = "",
            userUpdate = "",
            updateDate = ""
        ))
    )
    private val _lastScannedBarcode = MutableStateFlow<String?>(null)
    val lastScannedBarcode: StateFlow<String?> = _lastScannedBarcode

    init {
        viewModelScope.launch {
//            savedStateHandle.getLiveData<String?>(BARCODE_SCAN_RESULT_KEY, null)
//                .onEach { value ->
//                    Log.d("PrintVM_Debug", "SavedStateHandle Flow - Nueva emisión para '$BARCODE_SCAN_RESULT_KEY': $value")
//                }
//                .collect { codeBar ->
//                    if (codeBar!=null) {
//                        _lastScannedBarcode.value= codeBar
//                        Log.e("BarcodeResult",codeBar)
//                        savedStateHandle[BARCODE_SCAN_RESULT_KEY] = null
//                    }
//                }
//            val initialBarcode = savedStateHandle.get<String?>(BARCODE_SCAN_RESULT_KEY)
//            Log.d("PrintVM_Debug", "Valor inicial de SavedStateHandle para '$BARCODE_SCAN_RESULT_KEY' al init: $initialBarcode")
//            _lastScannedBarcode.value = initialBarcode

        }
    }

    val pesajeResult: StateFlow<Result<ImobPasaje>> = _pesajeResult

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Función para obtener el pesaje desde el repo
    fun obtenerPesaje(codeBar: String) {
        viewModelScope.launch {
            _loading.value = true
            pesajeRespository.getPesaje(codeBar)
                .catch { e ->
                    _pesajeResult.value = Result.failure(e)
                    _loading.value = false
                }
                .collect { imobPasaje ->
                    _pesajeResult.value = Result.success(imobPasaje)
                    _loading.value = false
                }
        }
    }
}