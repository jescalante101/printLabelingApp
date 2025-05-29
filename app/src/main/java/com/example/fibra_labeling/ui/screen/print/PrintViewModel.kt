package com.example.fibra_labeling.ui.screen.print

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.model.PrintResponse
import com.example.fibra_labeling.data.repository.PesajeRespository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

const val BARCODE_SCAN_RESULT_KEY = "barcode_scan_result"
class PrintViewModel(
    private val pesajeRespository: PesajeRespository,
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
            docEntry = 0,
            updateDate = ""
        ))
    )
    val pesajeResult: StateFlow<Result<ImobPasaje>> = _pesajeResult

    private val _lastScannedBarcode = MutableStateFlow<String?>("")
    val lastScannedBarcode: StateFlow<String?> = _lastScannedBarcode

    private val _printResult = MutableStateFlow<Result<PrintResponse>>(Result.success(
        PrintResponse(
            message = "",
            success = false,
            data = null
        )
    ))

    val printResult: StateFlow<Result<PrintResponse>> = _printResult

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _isPrintLoading = MutableStateFlow(false)
    val isPrintLoading: StateFlow<Boolean> = _isPrintLoading

    private val _isPrint = MutableStateFlow(false)
    val isPrint: StateFlow<Boolean> = _isPrint

    // Función para obtener el pesaje desde el repo
    fun obtenerPesaje(codeBar: String) {
        viewModelScope.launch {
            _loading.value = true
            _isPrint.value=false
            pesajeRespository.getPesaje(codeBar)
                .catch { e ->
                    _pesajeResult.value = Result.failure(e)
                    _loading.value = false
                    _isPrint.value=false
                }
                .collect { imobPasaje ->
                    _pesajeResult.value = Result.success(imobPasaje)
                    _loading.value = false
                    _isPrint.value=true
                }
        }
    }
    fun printPesaje(codeBar: String) {
        val codeBarValue = CodeBarRequest(codeBar)
        viewModelScope.launch {
            _isPrintLoading.value = true
            pesajeRespository.printPesaje(codeBarValue)
                .catch { e ->
                    _printResult.value = Result.failure(e)
                    _isPrintLoading.value = false
                }
                .collect {
                    _printResult.value = Result.success(it)
                    _isPrintLoading.value = false
                }
        }
    }



    fun actualizarCodeBar(value: String){
        _lastScannedBarcode.value = value
    }
}