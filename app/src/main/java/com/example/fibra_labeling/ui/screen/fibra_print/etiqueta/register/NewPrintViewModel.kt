package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PesajeDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintIncDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOcrdDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOitmDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOwhsDao
import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POwhsEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import com.example.fibra_labeling.data.local.mapper.fibraprint.toMap
import com.example.fibra_labeling.data.model.fibrafil.ZplPrintRequest
import com.example.fibra_labeling.data.model.fibrafil.toZplMap
import com.example.fibra_labeling.data.remote.fibrafil.FillRepository
import com.example.fibra_labeling.data.remote.fibrafil.SyncRepository
import com.example.fibra_labeling.data.utils.ZplTemplateMapper
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.datastore.UserLoginPreference
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.register.form.PrintFormStateNewLabel
import com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.util.generarCodigoBarras
import com.example.fibra_labeling.ui.util.generateStringCodeBar
import com.example.fibra_labeling.ui.util.getLocalDateNow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
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
    private val userLoginPreference: UserLoginPreference,
    private val oitmDao: PrintOitmDao,
    private val zplLabelDao: ZplLabelDao,
    private val repository: FillRepository
    ): ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading
    // filterProveedor
    val _filterProveedor= MutableStateFlow<String?>(null)
    val _filterAlmacen= MutableStateFlow<String?>(null)

    private val _eventoNavegacion = MutableSharedFlow<String>()
    // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

    var formState by mutableStateOf(PrintFormStateNewLabel())
        private set

    var userLogin by mutableStateOf("")
        private set

    private val _eventNotification = MutableSharedFlow<String>()
    // O usa un sealed class para destinos
    val eventNotification = _eventNotification.asSharedFlow()

    fun onCodigoChange(value: String) {
        formState = formState.copy(codigo = value, isValid = false)
    }



    fun onUnidadChange(unidad: String){
        formState = formState.copy(unidad = unidad, isValid = false)
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
    fun onObservacionesChange(value: String) {
        formState = formState.copy(observacion = value, isValid = false)
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

    fun getUserLogin(){
        viewModelScope.launch {
            userLogin = userLoginPreference.userName.firstOrNull() ?: ""
        }
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

    fun searchProveedor(){
        viewModelScope.launch {
            val product= oitmDao.getByCode(formState.codigo)
            if (product!=null){
                val prov=ocrdDao.findByCode(product.cardCode ?: "")
                if (prov!=null){
                    formState = formState.copy(proveedor = prov, isValid = false)
                }
            }
        }
    }

    fun saveLocal(isPrint:Boolean=false){
        viewModelScope.launch {
            _loading.value=true
            val user= userLoginPreference.userName.firstOrNull() ?: ""
            val docEntry= userLoginPreference.docEntry.firstOrNull() ?: ""
            val userCode= userLoginPreference.userCode.firstOrNull() ?: ""
            val peso=formState.pesoBruto.toDoubleOrNull() ?: 0.0
            val codebar=generateStringCodeBar(pesoDecimal = peso.toBigDecimal())
            val pesaje= PesajeEntity(
                peso = peso,
                fecha = getLocalDateNow!!,
                codigoBarra = codebar,
                proveedor = formState.proveedor?.cardName,
                lote = formState.lote,
                almacen = formState.almacen?.whsName,
                ubicacion = formState.ubicacion,
                piso = formState.piso,
                metroLineal = formState.metroLineal,
                u_area = formState.zona,
                nombre = formState.name,
                usuario = user,
                codigo = formState.codigo,
                unidad =formState.unidad,
            )

            _eventNotification.emit("saveSuccess")

            if (isPrint){
                val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
                val puerto = impresoraPrefs.impresoraPuerto.first()

                val zpl=zplLabelDao.getSelectedLabel()

                if (zpl==null){
                    _eventoNavegacion.emit("zplSetting")
                    _loading.value=false
                    return@launch
                }

                if (ip.isBlank() || puerto.isBlank()) {
                    _eventoNavegacion.emit("printSetting")
                    _loading.value=false
                    return@launch
                }

                printZpl(pesaje,ip,puerto.toInt(),zpl)
            }
            pesajeDao.insert(pesaje)

            if(user.isNotEmpty() && docEntry.isNotEmpty()){
                val diference= 0.0 - (formState.pesoBruto.toDoubleOrNull() ?:0.0)

                val inc= PIncEntity(
                    docEntry = docEntry.toLong(),
                    itemCode = formState.codigo,
                    itemName = formState.name,
                    whsCode = formState.almacen?.whsCode ?:"",
                    inWhsQty = 0.0,
                    countQty = formState.pesoBruto.toDoubleOrNull() ?: 0.0,
                    difference = diference,
                    ref2 = formState.unidad,
                    binLocation = formState.ubicacion,
                    uarea = formState.zona,
                    codeBar = codebar,
                    ref1 = formState.observacion,
                )
                pincDao.insert(inc)
            }
            delay(100)
           // reset()
            _loading.value=false


        }
    }



    private fun printZpl(pesaje: PesajeEntity, ip:String, puerto:Int, zpl: ZplLabel){
        viewModelScope.launch {


            val data= ZplPrintRequest(
                ip=ip,
                port=puerto,
                zplContent = ZplTemplateMapper.mapCustomTemplate(zpl.zplFile,pesaje.toMap(),1)
            )

            repository.filCustomPrintZpl(data).catch {e->
                Log.e("Error", e.message.toString())
                _eventoNavegacion.emit("errorPrint")
                _loading.value=false
            }.collect {
                _eventoNavegacion.emit("successPrint")
                _loading.value=false
            }

        }

    }

}
