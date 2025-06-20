package com.example.fibra_labeling.ui.screen.inventory.details


import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.repository.fibrafil.fibinc.FibIncRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.ZplLabelDao
import com.example.fibra_labeling.data.local.mapper.toProductoDetalleUi
import com.example.fibra_labeling.data.local.repository.fibrafil.EtiquetaDetalleRepository
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.model.fibrafil.FillPrintRequest
import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi
import com.example.fibra_labeling.data.model.fibrafil.ZplPrintRequest
import com.example.fibra_labeling.data.model.fibrafil.toZplMap
import com.example.fibra_labeling.data.remote.FillRepository
import com.example.fibra_labeling.data.utils.ZplTemplateMapper
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class IncViewModel(
    private val fibIncRepository: FibIncRepository,
    private val etiquetaDetalleRepository: EtiquetaDetalleRepository,
    private val impresoraPrefs: ImpresoraPreferences,
    private val fillRepository: FillRepository,
    private val fMaquinaRepository: FMaquinaRepository,
    private val zplLabelDao: ZplLabelDao
): ViewModel() {

    // Estado para el docEntry actualmente consultado (puede ser un parámetro mutable)
    private val _docEntry = MutableStateFlow<Int>(0)
    private val _search = MutableStateFlow<String?>(null)

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading


    private val _eventoNavegacion = MutableSharedFlow<String>()
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    // Método para cambiar el docEntry que deseas consultar
    fun setDocEntry(docEntry: Int) {
        _docEntry.value = docEntry
    }
    fun setSearch(query: String?) {
        _search.value = query
    }
    // Expón el Flow de los detalles según el docEntry seleccionado
    @OptIn(ExperimentalCoroutinesApi::class)
    val incData: StateFlow<List<FibIncEntity>> = _search
        .flatMapLatest { filter ->
            fibIncRepository.getByDocEntry(_docEntry.value, _search.value ?: "")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _productCode= MutableStateFlow<String>("")
    val productCode: MutableStateFlow<String> = _productCode

    private val _productName= MutableStateFlow<String>("")
    val productName: MutableStateFlow<String> = _productName

    private val _conteo= MutableStateFlow<String>("")
    val conteo: MutableStateFlow<String> = _conteo

    private val _itemSelected= MutableStateFlow<FibIncEntity>(FibIncEntity(
        id = 0,
        U_CountQty = 0.0,
        U_Difference = 0.0,
        U_InWhsQty = 0.0,
        U_ItemCode = "",
        U_ItemName = "",
        U_WhsCode = "",
        isSynced = false,
        docEntry = 0
    ))
    val itemSelected: MutableStateFlow<FibIncEntity> = _itemSelected

    fun onItemSelectedChange(itemSelected: FibIncEntity) {
        _itemSelected.value = itemSelected
    }
    fun onConteoChange(conteo: String) {
        val diference= itemSelected.value.U_InWhsQty?.minus(conteo.toDouble())
        _itemSelected.value= itemSelected.value.copy(U_CountQty = conteo.toDouble(), U_Difference = diference)
    }


    fun updateConteo(){
        viewModelScope.launch {
            fibIncRepository.update(_itemSelected.value)
        }
    }
    var ultimoEliminado: FibIncEntity? = null
    fun deleteItem(item: FibIncEntity){
        viewModelScope.launch {
            ultimoEliminado = item
            fibIncRepository.delete(item)
        }

    }

    fun restaurarUltimoEliminado() {
        ultimoEliminado?.let { inc ->
            viewModelScope.launch {
                fibIncRepository.insert(inc)
                ultimoEliminado = null
            }
        }
    }

    fun getEtiquetaBYWhsAndItemCode(whsCode: String, itemCode: String){
        viewModelScope.launch {
            _loading.value=true
            val ip = impresoraPrefs.impresoraIp.first() // suspende hasta obtener el valor real
            val puerto = impresoraPrefs.impresoraPuerto.first()

            val zpl=zplLabelDao.getSelectedLabel()

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

            val etiqueta = etiquetaDetalleRepository.getDetailsByWhsAndItemCode(whsCode, itemCode)
            val maquina = fMaquinaRepository.getByCode(etiqueta?.u_FIB_MachineCode.toString())
            val data= etiqueta?.toProductoDetalleUi()

            val dataBody= ZplPrintRequest(
                ip=ip,
                port=puerto.toInt(),
                zplContent = ZplTemplateMapper.mapCustomTemplate(zpl.zplFile, data?.copy(
                    maquina=maquina?.name ?:"",
                    codBar = data.codigo
                )!!.toZplMap() ,1)
            )


            fillRepository.filCustomPrintZpl(
                dataBody
            ).catch {e->
                Log.e("Error", e.message.toString());
                _loading.value=false
                _message.emit("Error al imprimir ${e.message}")
            }.collect {
                _loading.value=false
                _eventoNavegacion.emit("successPrint")
                _message.emit("successPrint")
            }

        }
    }



}