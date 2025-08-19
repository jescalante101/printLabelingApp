package com.example.fibra_labeling.ui.screen.fibrafil.etiqueta.etiqueta

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel
import com.example.fibra_labeling.data.local.mapper.fibrafil.toProductoDetalleUi
import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import com.example.fibra_labeling.data.model.fibrafil.FillPrintRequest
import com.example.fibra_labeling.data.model.fibrafil.ZplPrintRequest
import com.example.fibra_labeling.data.model.fibrafil.toZplMap
import com.example.fibra_labeling.data.remote.fibrafil.FillRepository
import com.example.fibra_labeling.data.utils.ZplTemplateMapper
import com.example.fibra_labeling.datastore.EmpresaPrefs
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImpresionModelView(
    private val repository: FillRepository,
    private val impresoraPrefs: ImpresoraPreferences,
    private val etiquetaDetalleRepository: EtiquetaDetalleRepository,
    private val fMaquinaRepository: FMaquinaRepository,
    private val zplLabelDao: ZplLabelDao,
    private val empresaPrefs: EmpresaPrefs
): ViewModel() {
    // recuperamos la empresa,si es Fibrafil = 01 sino 02
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

    private val _productDetailResult = MutableStateFlow<Result<ProductoDetalleUi>>(
        Result.success(ProductoDetalleUi(
            codigo = "",
            productoName = "",
            lote = "",
            referencia = "",
            maquina = "",
            ubicacion = "",
            codeBar = ""
        ))
    )
    val productDetailResult: StateFlow<Result<ProductoDetalleUi>> = _productDetailResult
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _lastScannedBarcode = MutableStateFlow<String?>("")
    val lastScannedBarcode: StateFlow<String?> = _lastScannedBarcode

    private val _isPrintLoading = MutableStateFlow(false)
    val isPrintLoading: StateFlow<Boolean> = _isPrintLoading

    private val _eventoNavegacion = MutableSharedFlow<String>() // O usa un sealed class para destinos
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()
    private val _isPrint = MutableStateFlow(false)
    val isPrint: StateFlow<Boolean> = _isPrint

    private val _printResult = MutableStateFlow<Result< FilPrintResponse>>(Result.success(
        FilPrintResponse(
            message = "",
            success = false,
            data = null
        )
    ))

    val printResult: StateFlow<Result<FilPrintResponse>> = _printResult


    fun obtenerEtiqueta(codeBar: String) {
        viewModelScope.launch {
            _loading.value = true
            _isPrint.value=false
            val etiqueta= etiquetaDetalleRepository.getDetailsByWhsAndItemCode("CH3-RE",codeBar)
           if (etiqueta!=null){
               val maquina= fMaquinaRepository.getByCode(etiqueta.u_FIB_MachineCode.toString())
               val data=etiqueta.toProductoDetalleUi().copy(
                   maquina = maquina?.name ?:"",
                   codeBar = etiqueta.itemCode
               )
               _productDetailResult.value = Result.success(data)
               _loading.value = false
               _isPrint.value=true
           }else{

               repository.getOitwInfo(codeBar)
                   .catch { e ->
                       _productDetailResult.value = Result.failure(e)
                       _loading.value = false
                       _isPrint.value=false
                   }.collect { imobPasaje ->
                       _productDetailResult.value = Result.success(imobPasaje)
                       _loading.value = false
                       _isPrint.value=true
                   }
           }
        }
    }

    fun filPrintEtiqueta(copies: Int,zplTemplate: String?) {

        viewModelScope.launch {

            _isPrintLoading.value = true
            val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
            val puerto = impresoraPrefs.impresoraPuerto.first()
            var zpl=zplLabelDao.getSelectedLabel()

            var zplFile=zplTemplate

            if (zpl==null && zplTemplate==null){
                _printResult.value = Result.failure(Exception("Etiqueta no configurada para impresion"))
                _eventoNavegacion.emit("printSetting")
                _isPrintLoading.value=false
                return@launch
            }
            if (zplTemplate==null){
                zplFile=zpl?.zplFile
            }

            if (ip.isBlank() || puerto.isBlank()) {
                _printResult.value = Result.failure(Exception("Impresora no configurada para impresión"))
                _eventoNavegacion.emit("printSetting")
                _isPrintLoading.value=false
                return@launch
            }

            val template = ZplTemplateMapper.mapCustomTemplate(
                zplFile.toString(),
                _productDetailResult.value.getOrNull()?.copy(
                    codeBar = lastScannedBarcode.value.toString()
                )?.toZplMap() ?: emptyMap() ,copies)

//            val codeBarValue = CodeBarRequest(codeBar,ip,puerto.toInt())
            val body= FillPrintRequest(
                ipPrinter = ip,
                portPrinter = puerto.toInt(),
                data = _productDetailResult.value.getOrNull(),
                nroCopias = copies
            )
            val newBody= ZplPrintRequest(
                ip = ip,
                port = puerto.toInt(),
                zplContent = template
            )

            repository.filCustomPrintZpl(newBody)
                .catch { e ->
                    _printResult.value = Result.failure(e)
                    _isPrintLoading.value = false
                }

                .collect {

                    Log.e("PRINT",it.toString())
                    _printResult.value = Result.success(it)
                    _isPrintLoading.value = false
                }
        }

    }

    fun actualizarCodeBar(value: String){
        _lastScannedBarcode.value = value
    }

}