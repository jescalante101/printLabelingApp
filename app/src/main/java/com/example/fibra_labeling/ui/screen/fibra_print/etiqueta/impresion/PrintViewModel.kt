package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.impresion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PesajeDao
import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import com.example.fibra_labeling.data.local.mapper.fibraprint.toImobPesaje
import com.example.fibra_labeling.data.local.mapper.fibraprint.toMap
import com.example.fibra_labeling.data.local.mapper.fibraprint.toOitmData
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.ImobPesaje
import com.example.fibra_labeling.data.model.PrintResponse
import com.example.fibra_labeling.data.model.fibrafil.ZplPrintRequest
import com.example.fibra_labeling.data.remote.PesajeRepository
import com.example.fibra_labeling.data.remote.fibrafil.FillRepository
import com.example.fibra_labeling.data.utils.ZplTemplateMapper
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.datastore.UserLoginPreference
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

const val BARCODE_SCAN_RESULT_KEY = "barcode_scan_result"
class PrintViewModel(
    private val pesajeRepository: PesajeRepository,
    private val impresoraPrefs: ImpresoraPreferences,
    private val pesajeDao: PesajeDao,
    private val userLoginPreference: UserLoginPreference,
    private val zplLabelDao: ZplLabelDao,
    private val fillRepository: FillRepository

): ViewModel() {
    // Estado de resultado de la consulta
    private val _pesajeResult = MutableStateFlow<Result<ImobPesaje>>(
        Result.success(ImobPesaje(
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
            docEntry = 0,
            updateDate = ""
        ))
    )

    init {
        viewModelScope.launch {
            userLoginPreference.saveUserLogin("","","")
        }
    }
    val pesajeResult: StateFlow<Result<ImobPesaje>> = _pesajeResult

    private val _lastScannedBarcode = MutableStateFlow<String?>("")
    val lastScannedBarcode: StateFlow<String?> = _lastScannedBarcode

    private val _printResult = MutableStateFlow<Result<PrintResponse>>(Result.success(
        PrintResponse(
            message = "",
            success = false,
            data = null
        )
    ))

    val printResult: StateFlow<Result<PrintResponse>> = _printResult

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _isPrintLoading = MutableStateFlow(false)
    val isPrintLoading: StateFlow<Boolean> = _isPrintLoading

    private val _isPrint = MutableStateFlow(false)
    val isPrint: StateFlow<Boolean> = _isPrint


    private val _eventoNavegacion = MutableSharedFlow<String>() // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

    private val _printMessage = MutableSharedFlow<String>() // O usa un sealed class para destinos
    val printMessage = _printMessage.asSharedFlow()



    // Función para obtener el pesaje desde el repo
    fun obtenerPesaje(codeBar: String) {
        viewModelScope.launch {
            _loading.value = true
            _isPrint.value=false

            val pesaje=pesajeDao.getById(codeBar)
            if (pesaje!=null){
                _pesajeResult.value = Result.success(pesaje.toImobPesaje())
                _loading.value = false
                _isPrint.value=true
            }else{
                pesajeRepository.getPesaje(codeBar)
                    .catch { e ->
                        _pesajeResult.value = Result.failure(e)
                        _loading.value = false
                        _isPrint.value=false
                    }
                    .collect { imobPasaje ->
                        _pesajeResult.value = Result.success(imobPasaje)
                        inserPesaje(imobPasaje)
                        _loading.value = false
                        _isPrint.value=true
                    }
            }
        }
    }

    private fun inserPesaje(imobPesaje: ImobPesaje){
        val pesaje= PesajeEntity(
            peso = imobPesaje.peso ?: 0.0,
            fecha = imobPesaje.createDate ?: "",
            codigoBarra = imobPesaje.codeBar,
            almacen = imobPesaje.almacen,
            usuario = imobPesaje.userCreate,
            proveedor = imobPesaje.proveedor,
            lote = imobPesaje.lote,
            ubicacion = imobPesaje.ubicacion,
            piso =imobPesaje.piso,
            metroLineal = imobPesaje.metroLineal,
            nombre = imobPesaje.name,
            u_area = imobPesaje.area,
            codigo = imobPesaje.itemCode,
            unidad = "",
            isSynced = true
        )
        viewModelScope.launch {
            pesajeDao.insert(pesaje)
        }
    }

    fun printPesaje(codeBar: String) {

        viewModelScope.launch {

            _isPrintLoading.value = true

            val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
            val puerto = impresoraPrefs.impresoraPuerto.first()

            if (ip.isBlank() || puerto.isBlank()) {
                _printResult.value = Result.failure(Exception("Impresora no configurada para impresión"))
                _eventoNavegacion.emit("printSetting")
                return@launch
            }

            val zpl=zplLabelDao.getSelectedLabel()

            if (zpl==null){
                _eventoNavegacion.emit("zplSetting")
                _loading.value=false
                return@launch
            }

            val etiqueta = pesajeDao.getById( codeBar)

            val data= etiqueta?.toMap()

            val zplContentLocal= ZplTemplateMapper.mapCustomTemplate(
                zpl.zplFile,
                data!!
            )
            val dataBody= ZplPrintRequest(
                ip=ip,
                port=puerto.toInt(),
                zplContent = zplContentLocal
            )

            fillRepository.filCustomPrintZpl(
                dataBody
            ).catch {e->
                Log.e("Error", e.message.toString());
               _printMessage.emit("PrintError")
                _isPrintLoading.value = false
            }.collect {
                _eventoNavegacion.emit("successPrint")
                _printMessage.emit("successPrint")
                _isPrintLoading.value = false
            }


        }




    }

    fun actualizarCodeBar(value: String){
        _lastScannedBarcode.value = value
    }
}