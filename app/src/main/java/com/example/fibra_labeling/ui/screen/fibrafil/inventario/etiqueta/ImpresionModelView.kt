package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiqueta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.model.ProductoDetalleUi
import com.example.fibra_labeling.data.remote.FillRepository
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
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

    fun actualizarCodeBar(value: String){
        _lastScannedBarcode.value = value
    }

}