package com.example.fibra_labeling.ui.screen

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.repository.PesajeRespository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val pesajeRespository: PesajeRespository): ViewModel() {
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
    val pesajeResult: StateFlow<Result<ImobPasaje>> = _pesajeResult

    // Estado loading para mostrar spinner
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