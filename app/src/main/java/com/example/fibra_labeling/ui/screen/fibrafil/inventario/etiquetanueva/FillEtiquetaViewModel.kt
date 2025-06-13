package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.MaquinaData
import com.example.fibra_labeling.data.model.PesajeResponse
import com.example.fibra_labeling.data.model.ProductoDetalleUi
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import com.example.fibra_labeling.data.remote.FillRepository
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.AddEtiquetaFormErrorState
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.AddEtiquetaFormState
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.hasError
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.validateAddEtiquetaForm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.collections.map

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

    private val _updateResponse= MutableStateFlow<FilPrintResponse>(
        FilPrintResponse(
           data = null,
            message = "",
            success = false
        )
    )
    val updateResponse: MutableStateFlow<FilPrintResponse> = _updateResponse

    private val _print= MutableStateFlow<Result<FilPrintResponse>>(
        Result.success(
            FilPrintResponse(
                data = null,
                message = "",
                success = false
            )
        )
    )

    val print: MutableStateFlow<Result<FilPrintResponse>> = _print


    private val _maquinas= MutableStateFlow<List<MaquinaData>>(
        listOf()
    )
    val maquinas: MutableStateFlow<List<MaquinaData>> = _maquinas

    private val _eventoNavegacion = MutableSharedFlow<String>() // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

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

    fun onMaquinaChange(newMaquina: MaquinaData) {
        formState = formState.copy(maquina = newMaquina)
        validate()
    }
    fun onCantidadChange(newCantidad: String) {
        formState = formState.copy(cantidad = newCantidad)
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
                    formState.maquina?.code ?:"",
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
                    it.map {
                        if (it.whsCode=="CH3-RE"){
                            onAlmacenChange(it)
                        }
                    }
                    _loading.value = false
                }
        }
    }
    fun getMaquinas(){
        viewModelScope.launch {
            _loading.value = true
            repository.getMaquinas("",1,200)
                .catch { e ->
                    Log.e("Error Almacen", e.message.toString())
                    _maquinas.value = listOf()
                    _loading.value = false
                }
                .collect {
                    Log.e("Success Almacen", it.data?.map { it?.name }.toString())
                    _maquinas.value = it.data as List<MaquinaData>
                    _loading.value = false
                }
        }
    }

    fun updateOitw(){
        viewModelScope.launch {
            _loading.value=true
            val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
            val puerto = impresoraPrefs.impresoraPuerto.first()

            if (ip.isBlank() || puerto.isBlank()) {
                _eventoNavegacion.emit("printSetting")
                _loading.value=true
                return@launch
            }
            val oitm= ProductoDetalleUi(
                codigo = formState.codigo,
                productoName = formState.producto,
                lote = formState.lote,
                referencia = formState.codigoReferencia,
                maquina = formState.maquina?.code,
                ubicacion = formState.ubicacion,
                whsCode = "CH3-RE",
                codBar = "", //TODO: implementar codigo de barras
            )
            repository.updateOitwInfo(oitm)
                .catch { e ->
                    Log.e("Error Almacen", e.message.toString())
                    _updateResponse.value = FilPrintResponse(data = null, message = e.message.toString(), success = false)
                    _loading.value = false
                }
                .collect {insert->
//                    _updateResponse.value = insert
//                    _loading.value = false
                    repository.filPrintEtiqueta(CodeBarRequest(formState.codigo.toString(),ip,puerto.toInt())).catch {e->
                        Log.e("Error", e.message.toString());
                        _loading.value=false
                        _print.value=Result.failure(e)
                    }.collect {
                        _loading.value=false
                        _print.value=Result.success(insert)
                    }
                }
        }


    }

}