package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiqueta

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.model.PrintResponse
import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import com.example.fibra_labeling.data.model.fibrafil.FillPrintRequest
import com.example.fibra_labeling.data.remote.FillRepository
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ImpresionModelView(private val repository: FillRepository, private val impresoraPrefs: ImpresoraPreferences): ViewModel() {
    private val _productDetailResult = MutableStateFlow<Result<ProductoDetalleUi>>(
        Result.success(ProductoDetalleUi(
            codigo = "",
            productoName = "",
            lote = "",
            referencia = "",
            maquina = "",
            ubicacion = "",
            codBar = ""
        ))
    )
    val productDetailResult: StateFlow<Result<ProductoDetalleUi>> = _productDetailResult
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _lastScannedBarcode = MutableStateFlow<String?>("")
    val lastScannedBarcode: StateFlow<String?> = _lastScannedBarcode

    private val _isPrintLoading = MutableStateFlow(false)
    val isPrintLoading: StateFlow<Boolean> = _isPrintLoading

    private val _eventoNavegacion = MutableSharedFlow<String>() // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()
    private val _isPrint = MutableStateFlow(false)
    val isPrint: StateFlow<Boolean> = _isPrint

    private val _printResult = MutableStateFlow<Result< FilPrintResponse>>(Result.success(
        FilPrintResponse(
            message = "",
            success = false,
            data = null
        )
    ))

    val printResult: StateFlow<Result<FilPrintResponse>> = _printResult


    fun obtenerEtiqueta(codeBar: String) {
        viewModelScope.launch {
            _loading.value = true
            _isPrint.value=false
            repository.getOitwInfo(codeBar)
                .catch { e ->
                    _productDetailResult.value = Result.failure(e)
                    _loading.value = false
                    _isPrint.value=false
                }.collect { imobPasaje ->
                    _productDetailResult.value = Result.success(imobPasaje)
                    _loading.value = false
                    _isPrint.value=true
                }
        }
    }

    fun filPrintEtiqueta(codeBar: String) {
        viewModelScope.launch {

            _isPrintLoading.value = true

            val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
            val puerto = impresoraPrefs.impresoraPuerto.first()

            if (ip.isBlank() || puerto.isBlank()) {
                _printResult.value = Result.failure(Exception("Impresora no configurada para impresión"))
                _eventoNavegacion.emit("printSetting")
                _isPrintLoading.value=false
                return@launch
            }

//            val codeBarValue = CodeBarRequest(codeBar,ip,puerto.toInt())
            val body= FillPrintRequest(
                ipPrinter = ip,
                portPrinter = puerto.toInt(),
                data = _productDetailResult.value.getOrNull()
            )
            repository.filPrintEtiqueta(body)
                .catch { e ->
                    _printResult.value = Result.failure(e)
                    _isPrintLoading.value = false
                }

                .collect {

                    Log.e("PRINT",it.toString())
                    _printResult.value = Result.success(it)
                    _isPrintLoading.value = false
                }
        }


    }

    fun actualizarCodeBar(value: String){
        _lastScannedBarcode.value = value
    }

}