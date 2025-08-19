package com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiquetanueva

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibrafil.FilAlmacenDao
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel
import com.example.fibra_labeling.data.local.mapper.fibrafil.toApiData
import com.example.fibra_labeling.data.local.mapper.fibrafil.toEtiquetaDetalleEntity
import com.example.fibra_labeling.data.local.mapper.fibrafil.toResponse
import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.fibinc.FibIncRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.MaquinaData
import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import com.example.fibra_labeling.data.model.fibrafil.ZplPrintRequest
import com.example.fibra_labeling.data.model.fibrafil.toZplMap
import com.example.fibra_labeling.data.remote.fibrafil.FillRepository
import com.example.fibra_labeling.data.utils.ZplTemplateMapper
import com.example.fibra_labeling.datastore.EmpresaPrefs
import com.example.fibra_labeling.datastore.GeneralPreference
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.datastore.UserLoginPreference
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiquetanueva.form.AddEtiquetaFormErrorState
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiquetanueva.form.AddEtiquetaFormState
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiquetanueva.form.hasError
import com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiquetanueva.form.validateAddEtiquetaForm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.map

class FillEtiquetaViewModel(
    private val repository: FillRepository,
    private val impresoraPrefs: ImpresoraPreferences,
    private val etiquetaDetalleRepository: EtiquetaDetalleRepository,
    private val localRepository: FMaquinaRepository,
    private val userLoginPreference: UserLoginPreference,
    private val fibIncRepository: FibIncRepository,
    private val zplLabelDao: ZplLabelDao,
    private val almacenDao: FilAlmacenDao,
    private val empresaPrefs: EmpresaPrefs,
    private val generalPrefs: GeneralPreference
): ViewModel() {

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
    val maquinas: StateFlow<List<MaquinaData>> = _maquinas

    private val _eventoNavegacion = MutableSharedFlow<String>()
    // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

    private val _user= MutableStateFlow<String>("")
    val user: MutableStateFlow<String> = _user

    private val _docEntry= MutableStateFlow<String>("")
    val docEntry: MutableStateFlow<String> = _docEntry

    val empresa = empresaPrefs.empresaId.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "01"
    )

    val labels: StateFlow<List<ZplLabel>> = zplLabelDao.getAllLabels(empresa.value)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val conteoMode: StateFlow<Boolean> =flow {
        generalPrefs.conteoUseMode.catch {
            emit(false)
        }.collect {
            emit(it)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)



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

    fun onStockChange(newStock: String) {
        formState = formState.copy(conteo = newStock)
        validate()
    }


    private fun validate() {
        errorState = validateAddEtiquetaForm(formState)
    }

    fun isFormValid(): Boolean =
        !errorState.hasError() &&
                listOf(
                    formState.almacen?.whsCode ?:"",
                    formState.codigoReferencia,
                    formState.cantidad,
                ).all { it.isNotBlank()  }

    fun getAlmacens(){
        viewModelScope.launch {
            _loading.value = true
            val almacenData=almacenDao.getAll()
            if (almacenData.isNotEmpty()){
                _almacens.value=almacenData.map { it.toResponse() }
                val select=almacenData.find {
                    it.whsCode=="CH3-RE"
                }
               if (select!=null){
                   onAlmacenChange(select.toResponse())
               }
                _loading.value = false
                //return@launch
            }else{
                _loading.value = false
                //return@launch
            }

//            repository.getAlmacens()
//                .catch { e ->
//                    Log.e("Error Almacen", e.message.toString())
//                    _almacens.value = listOf()
//                    _loading.value = false
//                }
//                .collect {
//                    Log.e("Success Almacen", it.map { it.whsName }.toString())
//                    _almacens.value = it
//                    it.map {
//                        if (it.whsCode=="CH3-RE"){
//                            onAlmacenChange(it)
//                        }
//                    }
//                    _loading.value = false
//                }
        }
    }

    fun searchMaquina(code: String,name:String){
        viewModelScope.launch {
            localRepository.searchByNameAndCode(name,code).collect{result->
                _maquinas.value=result.map { it.toApiData() }
            }

            //_maquinas.value=result.map { it.toApiData() }
        }
    }

    fun updateOitw(withPrinter:Boolean=false){
        viewModelScope.launch {
            _loading.value=true
            val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
            val puerto = impresoraPrefs.impresoraPuerto.first()

            if ((ip.isBlank() || puerto.isBlank() )&& withPrinter) {
                _eventoNavegacion.emit("printSetting")
                _loading.value=false
                return@launch
            }
            // 1. Validar/convertir cantidad
            val cantidad = formState.cantidad.toDoubleOrNull() ?: 0.0

            // 2. Crear tu modelo UI (como ya hacías antes)
            val oitm = ProductoDetalleUi(
                codigo = formState.codigo,
                productoName = formState.producto,
                lote = formState.lote,
                referencia = formState.codigoReferencia,
                maquina = formState.maquina?.code,
                ubicacion = formState.ubicacion,
                whsCode = formState.almacen?.whsCode ?: "CH3-RE",
                codeBar = "" // TODO: Generar o asignar el código de barras real aquí si lo tienes
            )

            // 3. Mapear a la entidad Room
            val entity = oitm.toEtiquetaDetalleEntity(
                cantidad = formState.cantidad,
                isSynced = false // Siempre false al guardar local
            )

            // 4. Guardar localmente usando el repository local (Room)
            etiquetaDetalleRepository.insert(entity)
            var stock=0.0
            if (formState.conteo.isEmpty()){
                stock=formState.conteo.toDoubleOrNull() ?: 0.0
            }
            Log.e("ASKJDHKASJHDJKAS",stock.toString())

            if (_docEntry.value.isNotEmpty()){
                saveInc(stock,_docEntry.value.toInt())
            }

            // 5. Acciones post-guardado
           if (withPrinter){
               _eventoNavegacion.emit("savedLocal") // Ejemplo: navega o muestra mensaje
           }else{
               _eventoNavegacion.emit("savedLocalNoPrint") // Ejemplo: navega o muestra mensaje
           }

            _loading.value = false
        }
    }

    fun printEtiqueta(nro: Int,zpl: String){
        viewModelScope.launch {

            _loading.value=true
            val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
            val puerto = impresoraPrefs.impresoraPuerto.first()

            //val zpl=zplLabelDao.getSelectedLabel()

            if (zpl==null){
                _eventoNavegacion.emit("printSetting")
                _loading.value=false
                return@launch
            }

            if (ip.isBlank() || puerto.isBlank()) {
                _eventoNavegacion.emit("printSetting")
                _loading.value=false
                return@launch
            }

                           val  details = ProductoDetalleUi(
                    codigo = formState.codigo,
                    productoName = formState.producto,
                    lote = formState.lote,
                    referencia = formState.codigoReferencia,
                    maquina = formState.maquina?.name,
                    ubicacion = formState.ubicacion,
                    whsCode = formState.almacen?.whsCode ?: "CH3-RE",
                    codeBar = formState.codigo
                )

            val data= ZplPrintRequest(
                ip=ip,
                port=puerto.toInt(),
                zplContent = ZplTemplateMapper.mapCustomTemplate(zpl,details.toZplMap(),nro)
            )


            repository.filCustomPrintZpl(data).catch {e->
                Log.e("Error", e.message.toString());
                _loading.value=false
                _print.value=Result.failure(e)
            }.collect {
                _loading.value=false
                _print.value=Result.success(it)
                _eventoNavegacion.emit("successPrint")
            }
        }

    }

    fun getUserLogin(){
        viewModelScope.launch {
            _user.value=userLoginPreference.userName.firstOrNull() ?: ""
            _docEntry.value=userLoginPreference.docEntry.firstOrNull() ?: ""
        }
    }

    private fun saveInc(stock: Double,docEntry:Int){
        viewModelScope.launch {
            val cantidad=formState.cantidad.toDoubleOrNull()?:0.0
            val diference= stock - cantidad

            val entity= FibIncEntity(
                U_Difference = diference,
                U_WhsCode = formState.almacen?.whsCode,
                U_CountQty = formState.cantidad.toDoubleOrNull(),
                U_InWhsQty = stock,
                U_ItemCode = formState.codigo,
                U_ItemName = formState.producto,
                isSynced = false,
                docEntry = docEntry
            )
            fibIncRepository.insert(entity)
        }
    }


}