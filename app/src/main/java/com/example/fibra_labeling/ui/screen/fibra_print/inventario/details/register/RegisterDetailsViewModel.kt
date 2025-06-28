package com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PesajeDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintIncDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOcrdDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOwhsDao
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POwhsEntity
import com.example.fibra_labeling.datastore.UserLoginPreference
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.register.form.DetailsFormState
import com.example.fibra_labeling.ui.util.generateStringCodeBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrintRegisterIncDetailsViewModel(
    private val printIncDao: PrintIncDao,
    private val pesajeDao: PesajeDao,
    private val ocrDao: PrintOcrdDao,
    private val owhsDao: PrintOwhsDao,
    private val userLoginPreference: UserLoginPreference
): ViewModel() {
    private val _formState = MutableStateFlow(DetailsFormState())
    val formState: StateFlow<DetailsFormState> = _formState.asStateFlow()

    fun onObservacionChange(newObservacion: String) {
        _formState.update { currentState ->
            currentState.copy(observacion = newObservacion)
        }
    }

    fun onConteoChange(newConteo: String) {
        _formState.update { currentState ->
            currentState.copy(conteo = newConteo)
        }
    }

    // --- Nuevas funciones para 'ubicacion' ---
    fun onUbicacionChange(newUbicacion: String) {
        _formState.update { currentState ->
            currentState.copy(ubicacion = newUbicacion)
        }
    }

    // Funciones para establecer campos de solo lectura si vienen de otra fuente
    fun setCodigo(newCodigo: String) {
        _formState.update { currentState ->
            currentState.copy(codigo = newCodigo)
        }
    }

    fun setNombreProducto(newName: String) {
        _formState.update { currentState ->
            currentState.copy(nombreProducto = newName)
        }
    }

    fun setProveedor(newProveedor: POcrdEntity) {
        _formState.update { currentState ->
            currentState.copy(proveedor = newProveedor)
        }
    }

    fun setAlmacen(newAlmacen: POwhsEntity) {
        _formState.update { currentState ->
            currentState.copy(almacen = newAlmacen)
        }
    }

    // Funciones para los botones de conteo
    fun incrementarConteo() {
        _formState.update { currentState ->
            val currentCount = currentState.conteo.toIntOrNull() ?: 0
            currentState.copy(conteo = (currentCount + 1).toString())
        }
    }

    fun decrementarConteo() {
        _formState.update { currentState ->
            val currentCount = currentState.conteo.toIntOrNull() ?: 0
            if (currentCount > 0) {
                currentState.copy(conteo = (currentCount - 1).toString())
            } else {
                currentState
            }
        }
    }

    fun setCodeBar(codeBar: String){
        _formState.update { currentState ->
            currentState.copy(codeBar = codeBar)
        }
    }

    fun resetForm() {
        _formState.value = DetailsFormState()
    }

    ///Init
    init {
        loadData()
    }
    ///
    private fun loadData(){
        viewModelScope.launch {
            val codeBar = _formState.value.codeBar
            val pesaje= pesajeDao.getById(codeBar)
            if(pesaje == null){
                return@launch
            }
            val almacen=owhsDao.findByName(pesaje.almacen!!.trim())
            val proveedor=ocrDao.findByName(pesaje.proveedor!!.trim())

            setAlmacen(almacen!!)
            setProveedor(proveedor!!)

        }
    }

    fun saveInc(){
        viewModelScope.launch {
            val docEntry = userLoginPreference.docEntry.firstOrNull()

            val diference = 0.0.minus(formState.value.conteo.toDoubleOrNull() ?: 0.0)

            val inc= PIncEntity(
                docEntry = docEntry?.toLong() ?: 0,
                itemCode = formState.value.codigo,
                itemName = formState.value.nombreProducto,
                whsCode = formState.value.almacen?.whsCode ?:"",
                inWhsQty = 0.0,
                countQty = formState.value.conteo.toDoubleOrNull() ?: 0.0,
                difference = diference,
                binLocation = formState.value.pesaje?.ubicacion,
                uarea = formState.value.pesaje?.u_area,
                codeBar = _formState.value.codeBar,

            )
            printIncDao.insert(inc)

            resetForm()
        }
    }

}