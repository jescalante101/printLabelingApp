package com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PesajeDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintIncDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOcrdDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOwhsDao
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POwhsEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import com.example.fibra_labeling.datastore.UserLoginPreference
import com.example.fibra_labeling.ui.screen.fibra_print.inventario.details.register.form.DetailsFormState
import com.example.fibra_labeling.ui.util.generateStringCodeBar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
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

    val _filterProveedor= MutableStateFlow<String?>(null)
    val _filterAlmacen= MutableStateFlow<String?>(null)

    val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _eventoNavegacion = MutableSharedFlow<String>()
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

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

    fun onChangeMetroL(metro: String){
        _formState.update { currentState ->
            currentState.copy(metroLineal = metro)

        }
    }

    fun onChangeUnidad(unidad: String) {
        _formState.update { currentState ->
            currentState.copy(unidad = unidad)
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

        fun setCodeBar(codeBar: String) {
            _formState.update { currentState ->
                currentState.copy(codeBar = codeBar)
            }
        }

        fun resetForm() {
            _formState.value = DetailsFormState()
        }

        fun setFilter(filter: String) {
            _filterProveedor.value = filter
        }

        fun setFilterAlmacen(filter: String) {
            _filterAlmacen.value = filter
        }

    fun setPesaje(pesaje: PesajeEntity){
        _formState.update { currentState ->
            currentState.copy(pesaje = pesaje)
        }
    }
    fun setInc(inc: PIncEntity){
        _formState.update { currentState ->
            currentState.copy(inc = inc)
        }
    }

        @OptIn(ExperimentalCoroutinesApi::class)
        val allAlmacenes: StateFlow<List<POwhsEntity>> = _filterAlmacen
            .flatMapLatest { query ->
                owhsDao.buscarPorCodigoONombre(filtro = query ?: "")
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

        ///Init

        ///
        fun loadData() {
            viewModelScope.launch {
                try {
                    val codeBar = _formState.value.codeBar

                    val pesaje = pesajeDao.getById(codeBar)

                    val inc = printIncDao.getByCodeBar(codeBar)

                    if (pesaje == null) {
                        return@launch
                    }



                    val proveedor = ocrDao.findByName(pesaje.proveedor!!.trim())
                    val almacen = owhsDao.findByName(pesaje.almacen!!.trim())

                    onChangeUnidad("${pesaje.unidad}")

                    if (almacen != null) {
                        setAlmacen(almacen)
                        Log.e("Almacen",almacen.toString())

                    }
                    if (proveedor!=null){
                        setProveedor(proveedor)
                    }

//                setAlmacen(almacen!!)
//                setProveedor(proveedor!!)
                    setPesaje(pesaje)
                    if(inc!=null){
                        setInc(inc)
                    }
                }catch (
                    e: Exception
                ){}


            }
        }

        fun saveInc() {
            viewModelScope.launch {
                _loading.value=true
                val docEntry = userLoginPreference.docEntry.firstOrNull()

                val diference = 0.0.minus(formState.value.conteo.toDoubleOrNull() ?: 0.0)
                var inc= formState.value.inc
                if (inc!=null){
                    inc = inc.copy(
                        itemCode = formState.value.codigo,
                        itemName = formState.value.nombreProducto,
                        whsCode = formState.value.almacen?.whsCode ?: "",
                        inWhsQty = 0.0,
                        countQty = formState.value.conteo.toDoubleOrNull() ?: 0.0,
                        difference = diference,
                        binLocation = formState.value.pesaje?.ubicacion,
                        uarea = formState.value.pesaje?.u_area,
                        codeBar = _formState.value.codeBar,
                        ref2 = _formState.value.unidad,
                        ref1 = _formState.value.observacion
                    )
                    printIncDao.update(inc)

                }else{
                    val newInc = PIncEntity(
                        docEntry = docEntry!!.toLong(),
                        itemCode = formState.value.codigo,
                        itemName = formState.value.nombreProducto,
                        whsCode = formState.value.almacen?.whsCode ?: "",
                        inWhsQty = 0.0,
                        countQty = formState.value.conteo.toDoubleOrNull() ?: 0.0,
                        difference = diference,
                        binLocation = formState.value.pesaje?.ubicacion,
                        uarea = formState.value.pesaje?.u_area,
                        codeBar = _formState.value.codeBar,
                        ref2 = _formState.value.unidad,
                        ref1 = _formState.value.observacion
                    )
                    printIncDao.insert(newInc)
                }
                //Update Pesaje
//                val pesaje= formState.value.pesaje?.copy(
//                    metroLineal = formState.value.metroLineal,
//                    peso = formState.value.conteo.toDoubleOrNull() ?: 0.0
//                )
//                pesajeDao.update(pesaje!!)

                delay(100)
                resetForm()
                _eventoNavegacion.emit("saved")
                _loading.value=false
            }
        }

}