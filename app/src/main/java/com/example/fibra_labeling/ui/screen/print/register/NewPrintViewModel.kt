package com.example.fibra_labeling.ui.screen.print.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.PesajeRequest
import com.example.fibra_labeling.data.model.PesajeResponse
import com.example.fibra_labeling.data.model.ProveedorResponse
import com.example.fibra_labeling.data.remote.OitmRepository
import com.example.fibra_labeling.data.remote.PesajeRepository
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.ui.screen.print.register.form.FormErrorState
import com.example.fibra_labeling.ui.screen.print.register.form.FormState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import kotlin.Result

class NewPrintViewModel(
    private val repository: OitmRepository,
    private val pesajeRepository: PesajeRepository,
    private val impresoraPrefs: ImpresoraPreferences
    ): ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading

    private val _proveedorName= MutableStateFlow<Result<ProveedorResponse>>(
        Result.success(
            ProveedorResponse(
                cardName = ""
            )
    ))

    val proveedorName: MutableStateFlow<Result<ProveedorResponse>> = _proveedorName

    private val _almacens= MutableStateFlow<List<AlmacenResponse>>(
        listOf()
    )

    val almacenes: MutableStateFlow<List<AlmacenResponse>> = _almacens

    val motivos = listOf(
        Motivo("007", "INVENTARIO"),
        Motivo("001", "INGRESO POR COMPRA"),
        Motivo("002","INGRESO POR DEVOLUCION"),
        Motivo("003","INGRESO POR TRANSFERENCIA"),
        Motivo("004","SALIDA A PRODUCCION"),
        Motivo("005","SALIDA A REBOBINADO"),
        Motivo("006","SALIDA A HABILITADO"),

    )

    private val _formState= MutableStateFlow(FormState())
    val formState=_formState.asStateFlow()

    private val _formErrorState = MutableStateFlow(FormErrorState())
    val formErrorState = _formErrorState.asStateFlow()

    private val _print= MutableStateFlow<Result<PesajeResponse>>(
        Result.success(
            PesajeResponse(
                result = "",
                success = false,
                data = null,
                message = ""
            )
        )
    )
    val print: MutableStateFlow<Result<PesajeResponse>> = _print


    private val _eventoNavegacion = MutableSharedFlow<String>() // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

    fun getProveedorName(code: String){
        viewModelScope.launch {
            _loading.value = true
            repository.getProveedorName(code)
                .catch { e ->
                    _proveedorName.value = Result.failure(e)
                    onProveedorChange("")
                    _loading.value = false
                    Log.e("Error Proveedor", e.message.toString())
                }
                .collect {
                    _proveedorName.value = Result.success(it)
                    Result.success(it).getOrNull()?.cardName?.let { proveedor -> onProveedorChange(proveedor) }
                    _loading.value = false
                    Log.e("Success Proveedor", it.toString())
                }
        }
    }

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


    fun insertPesaje(){
        viewModelScope.launch {
            _loading.value=true

            val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
            val puerto = impresoraPrefs.impresoraPuerto.first()

            if (ip.isBlank() || puerto.isBlank()) {
                _eventoNavegacion.emit("printSetting")
                return@launch
            }

           if (formState.value.isValid){
               val request= PesajeRequest(
                   almacen = formState.value.almacen?.whsName,
                   codigo = formState.value.codigo,
                   equi = formState.value.equivalente,
                   lote = formState.value.lote,
                   mLineal = formState.value.metroLineal,
                   motivo = formState.value.motivo,
                   name = formState.value.name,
                   pesoBruto = formState.value.pesoBruto,
                   piso = formState.value.piso,
                   provee = formState.value.proveedor,
                   stepd = formState.value.stepd,
                   ubicacion = formState.value.ubicacion,
                   usuario = formState.value.usuario
               )
               repository.insertPesaje(request)
                   .catch {e->
                       Log.e("Error", e.message.toString());
                        _loading.value=false
                       _print.value=Result.failure(e)
                   }.collect { insert->

                       Log.e("Success", insert.toString())
                        val codeBar= insert.data?.codeBar

                       pesajeRepository.printPesaje(CodeBarRequest(codeBar.toString(),ip,puerto.toInt())).catch {e->
                           Log.e("Error", e.message.toString());
                           _loading.value=false
                           _print.value=Result.failure(e)
                       }.collect {
                           _loading.value=false
                           _print.value=Result.success(insert)
                       }
                   }
           }else{
               _loading.value=false
               _print.value=Result.failure(Exception("Formulario no valido"))
           }
        }

    }



    //VALIDACIONES DE FORMULARIO

    fun onCodigoChange(codigo: String){
        _formState.value = _formState.value.copy(codigo = codigo)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onNameChange(name: String){
        _formState.value = _formState.value.copy(name = name)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onProveedorChange(proveedor: String){
        _formState.value = _formState.value.copy(proveedor = proveedor)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onLoteChange(lote: String){
        _formState.value = _formState.value.copy(lote = lote)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onAlmacenChange(almacen: AlmacenResponse){
        _formState.value = _formState.value.copy(almacen = almacen)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onMotivoChange(motivo: String){
        _formState.value = _formState.value.copy(motivo = motivo)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onUbicacionChange(ubicacion: String){
        _formState.value = _formState.value.copy(ubicacion = ubicacion)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onPisoChange(piso: String){
        _formState.value = _formState.value.copy(piso = piso)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }

    fun onMetroLinealChange(metroLineal: String){
        _formState.value = _formState.value.copy(metroLineal = metroLineal)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }

    fun onEquivalenteChange(equivalente: String){
        _formState.value = _formState.value.copy(equivalente = equivalente)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onPesoBrutoChange(pesoBruto: String){
        _formState.value = _formState.value.copy(pesoBruto = pesoBruto)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onUsuarioChange(usuario: String){
        _formState.value = _formState.value.copy(usuario = usuario)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }
    fun onStepdChange(stepd: String){
        _formState.value = _formState.value.copy(stepd = stepd)
        val (newState, errorState) = validate(_formState.value)
        _formState.value = newState
        _formErrorState.value = errorState
    }


    fun validate(state: FormState): Pair<FormState, FormErrorState> {
        var isValid = true

        val almacenError = if (state.almacen?.whsName.isNullOrBlank()) {
            isValid = false
            "Seleccione un almacén"
        } else null

        val ubicacionError = if (state.ubicacion.isBlank()) {
            isValid = false
            "Ingrese una ubicación"
        } else null

        val motivoError = if (state.motivo.isBlank()) {
            isValid = false
            "Seleccione un motivo"
        } else null

        val pisoError = if (state.piso.isBlank()) {
            isValid = false
            "Seleccione un piso"
        } else null

        val pesoBrutoError = if (state.pesoBruto.isBlank()) {
            isValid = false
            "Ingrese el peso bruto"
        } else null

        val metroLinealError = if (state.metroLineal.isBlank()) {
            isValid = false
            "Ingrese el metro lineal"
        }else null
         val loteError = if (state.lote.isBlank()) {
             isValid=false
             "Ingrese el lote"
         }else null


        return state.copy(isValid = isValid) to FormErrorState(
            almacenError = almacenError,
            ubicacionError = ubicacionError,
            motivoError = motivoError,
            pisoError = pisoError,
            pesoBrutoError = pesoBrutoError,
            metroLinealError = metroLinealError,
            loteError = loteError
        )
    }




}

data class Motivo(
    val codigo: String,
    val descripcion: String
)