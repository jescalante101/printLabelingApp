package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.remote.FillRepository
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.AddEtiquetaFormErrorState
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.AddEtiquetaFormState
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.hasError
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.validateAddEtiquetaForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FillEtiquetaViewModel(private val repository: FillRepository,  private val impresoraPrefs: ImpresoraPreferences): ViewModel() {

    var formState by mutableStateOf(AddEtiquetaFormState())
        private set

    var errorState by mutableStateOf(AddEtiquetaFormErrorState())
        private set

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading

    private val _almacens= MutableStateFlow<List<AlmacenResponse>>(
        listOf()
    )

    val almacenes: MutableStateFlow<List<AlmacenResponse>> = _almacens

    // Funciones para cambiar cada campo
    fun onLoteChange(newLote: String) {
        formState = formState.copy(lote = newLote)
        validate()
    }

    fun onAlmacenChange(newAlmacen: AlmacenResponse) {
        formState = formState.copy(almacen = newAlmacen)
        validate()
    }

    fun onCodigoReferenciaChange(newCodigoReferencia: String) {
        formState = formState.copy(codigoReferencia = newCodigoReferencia)
        validate()
    }

    fun onMaquinaChange(newMaquina: String) {
        formState = formState.copy(maquina = newMaquina)
        validate()
    }

    fun onUbicacionChange(newUbicacion: String) {
        formState = formState.copy(ubicacion = newUbicacion)
        validate()
    }
    fun onCodigoChange(newCodigo: String) {
        formState = formState.copy(codigo = newCodigo)
        validate()
    }
    fun onProductoChange(newProducto: String) {
        formState = formState.copy(producto = newProducto)
        validate()
    }

    private fun validate() {
        errorState = validateAddEtiquetaForm(formState)
    }

    fun isFormValid(): Boolean =
        !errorState.hasError() &&
                listOf(
                    formState.lote,
                    formState.almacen?.whsCode ?:"",
                    formState.codigoReferencia,
                    formState.maquina,
                    formState.ubicacion
                ).all { it.isNotBlank()  }

    fun getAlmacens(){
        viewModelScope.launch {
            _loading.value = true
            repository.getAlmacens()
                .catch { e ->
                    Log.e("Error Almacen", e.message.toString())
                    _almacens.value = listOf()
                    _loading.value = false
                }
                .collect {
                    Log.e("Success Almacen", it.map { it.whsName }.toString())
                    _almacens.value = it
                    _loading.value = false
                }
        }
    }


}