package com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.mapper.toApiData
import com.example.fibra_labeling.data.local.mapper.toEtiquetaDetalleEntity
import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.fibinc.FibIncRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.MaquinaData
import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import com.example.fibra_labeling.data.model.fibrafil.FillPrintRequest
import com.example.fibra_labeling.data.model.fibrafil.StockResponse
import com.example.fibra_labeling.data.remote.FillRepository
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.datastore.UserLoginPreference
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.AddEtiquetaFormErrorState
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.AddEtiquetaFormState
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.hasError
import com.example.fibra_labeling.ui.screen.fibrafil.inventario.etiquetanueva.form.validateAddEtiquetaForm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.collections.map

class FillEtiquetaViewModel(
    private val repository: FillRepository,
    private val impresoraPrefs: ImpresoraPreferences,
    private val etiquetaDetalleRepository: EtiquetaDetalleRepository,
    private val localRepository: FMaquinaRepository,
    private val userLoginPreference: UserLoginPreference,
    private val fibIncRepository: FibIncRepository
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

    private val _eventoNavegacion = MutableSharedFlow<String>() // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

    private val _user= MutableStateFlow<String>("")
    val user: MutableStateFlow<String> = _user






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

    fun searchMaquina(code: String,name:String){
        viewModelScope.launch {
            localRepository.searchByNameAndCode(name,code).collect{result->
                _maquinas.value=result.map { it.toApiData() }
            }

            //_maquinas.value=result.map { it.toApiData() }
        }
    }

    fun updateOitw(){
        viewModelScope.launch {
            _loading.value = true

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
                codBar = "" // TODO: Generar o asignar el código de barras real aquí si lo tienes
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

            saveInc(stock)

            // 5. Acciones post-guardado
            _eventoNavegacion.emit("savedLocal") // Ejemplo: navega o muestra mensaje
            _loading.value = false
        }
    }

    fun printEtiqueta(){
        viewModelScope.launch {
            _loading.value=true
            val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
            val puerto = impresoraPrefs.impresoraPuerto.first()

            if (ip.isBlank() || puerto.isBlank()) {
                _eventoNavegacion.emit("printSetting")
                _loading.value=false
                return@launch
            }

            val data= FillPrintRequest(
                data = ProductoDetalleUi(
                    codigo = formState.codigo,
                    productoName = formState.producto,
                    lote = formState.lote,
                    referencia = formState.codigoReferencia,
                    maquina = formState.maquina?.name,
                    ubicacion = formState.ubicacion,
                    whsCode = formState.almacen?.whsCode ?: "CH3-RE",
                    codBar = "" // TODO: Generar o asignar el código de barras real aquí si lo tienes
                ),
                ipPrinter = ip,
                portPrinter = puerto.toInt()

            )
            repository.filPrintEtiqueta(data).catch {e->
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
        }
    }


    private fun saveInc(stock: Double){
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
            )

            fibIncRepository.insert(entity)
        }
    }


}