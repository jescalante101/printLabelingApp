package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PesajeDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintIncDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOcrdDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOwhsDao
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POwhsEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.datastore.UserLoginPreference
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.form.PrintFormStateNewLabel
import com.example.fibra_labeling.ui.util.getLocalDateNow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewPrintViewModel(
    private val impresoraPrefs: ImpresoraPreferences,
    private val ocrdDao: PrintOcrdDao,
    private val printOwhsDao: PrintOwhsDao,
    private val pesajeDao: PesajeDao,
    private val pincDao: PrintIncDao,
    private val userLoginPreference: UserLoginPreference
    ): ViewModel() {

    // filterProveedor
    val _filterProveedor= MutableStateFlow<String?>(null)
    val _filterAlmacen= MutableStateFlow<String?>(null)

    var formState by mutableStateOf(PrintFormStateNewLabel())
        private set

    private val _eventNotification = MutableSharedFlow<String>()
    // O usa un sealed class para destinos
    val eventNotification = _eventNotification.asSharedFlow()

    fun onCodigoChange(value: String) {
        formState = formState.copy(codigo = value, isValid = false)
    }

    fun onNameChange(value: String) {
        formState = formState.copy(name = value, isValid = false)
    }

    fun onProveedorChange(value: POcrdEntity?) {
        formState = formState.copy(proveedor = value, isValid = false)
    }

    fun onLoteChange(value: String) {
        formState = formState.copy(lote = value, isValid = false)
    }

    fun onAlmacenChange(value: POwhsEntity?) {
        formState = formState.copy(almacen = value, isValid = false)
    }

    fun onUbicacionChange(value: String) {
        formState = formState.copy(ubicacion = value, isValid = false)
    }

    fun onPisoChange(value: String) {
        formState = formState.copy(piso = value, isValid = false)
    }

    fun onMetroLinealChange(value: String) {
        formState = formState.copy(metroLineal = value, isValid = false)
    }

    fun onZonaChange(value: String) {
        formState = formState.copy(zona = value, isValid = false)
    }

    fun onPesoBrutoChange(value: String) {
        formState = formState.copy(pesoBruto = value, isValid = false)
    }

    fun onUsuarioChange(value: String) {
        formState = formState.copy(usuario = value, isValid = false)
    }

    fun reset() {
        formState = PrintFormStateNewLabel()
    }

    fun setFilter(filter:String){
        _filterProveedor.value = filter
    }

    fun setFilterAlmacen(filter:String){
        _filterAlmacen.value = filter
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val allProveedores: StateFlow<List<POcrdEntity>> = _filterProveedor
        .flatMapLatest { query ->
            ocrdDao.buscarPorCodigoONombre(filtro = query ?:"")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    @OptIn(ExperimentalCoroutinesApi::class)
    val allAlmacenes: StateFlow<List<POwhsEntity>> = _filterAlmacen
        .flatMapLatest { query ->
            printOwhsDao.buscarPorCodigoONombre(filtro = query ?:"")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun saveLocal(){
        viewModelScope.launch {

            val user= userLoginPreference.userName.firstOrNull() ?: ""
            val docEntry= userLoginPreference.docEntry.firstOrNull() ?: ""
            val userCode= userLoginPreference.userCode.firstOrNull() ?: ""

            val pesaje= PesajeEntity(
                peso = formState.pesoBruto.toDoubleOrNull() ?: 0.0,
                fecha = getLocalDateNow!!,
                codigoBarra = "",//TODO: Implentar codigo de barras
                proveedor = formState.proveedor?.cardName,
                lote = formState.lote,
                almacen = formState.almacen?.whsName,
                ubicacion = formState.ubicacion,
                piso = formState.piso,
                metroLineal = formState.metroLineal,
                u_area = formState.zona,
                nombre = formState.name,
                usuario = user,
            )

            val diference= 0.0 - (formState.pesoBruto.toDoubleOrNull() ?:0.0)

            val inc= PIncEntity(
                docEntry = docEntry.toLong(),
                itemCode = formState.codigo,
                itemName = formState.name,
                whsCode = formState.almacen?.whsCode ?:"",
                inWhsQty = 0.0,
                countQty = formState.pesoBruto.toDoubleOrNull() ?: 0.0,
                difference = diference,
                binLocation = formState.ubicacion,
                uarea = formState.zona,
            )

            pesajeDao.insert(pesaje)
            pincDao.insert(inc)
            reset()

            _eventNotification.emit("saveSuccess")

        }
    }

}
