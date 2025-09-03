package com.example.fibra_labeling.fibraprint.ui.screen.inventario.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PesajeDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintIncDao
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity
import com.example.fibra_labeling.data.local.mapper.fibraprint.toMap
import com.example.fibra_labeling.data.model.fibrafil.ZplPrintRequest
import com.example.fibra_labeling.data.remote.fibrafil.FillRepository
import com.example.fibra_labeling.data.utils.ZplTemplateMapper
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrintIncViewModel(
    private val printIncDao: PrintIncDao,
    private val impresoraPrefs: ImpresoraPreferences,
    private val zplLabelDao: ZplLabelDao,
    private val pesajeDao: PesajeDao,
    private val fillRepository: FillRepository,// mientras
): ViewModel() {
    private val _docEntry = MutableStateFlow<Int>(0)
    private val _search = MutableStateFlow<String?>("")

    private val _selectedItem = MutableStateFlow<PIncEntity?>(null)
    val selectedItem: StateFlow<PIncEntity?> = _selectedItem

    private val _loading = MutableStateFlow(false)
    val loading: MutableStateFlow<Boolean> = _loading

    private val _eventoNavegacion = MutableSharedFlow<String>()
    val eventoNavegacion = _eventoNavegacion.asSharedFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    fun setDocEntry(docEntry: Int) {
        _docEntry.value = docEntry
    }

    fun setSearch(query: String?) {
        _search.value = query
    }

    // selected item
    fun selectItem(item: PIncEntity) {
        _selectedItem.value = item
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val incData: StateFlow<List<PIncEntity>> = _search
        .flatMapLatest { filtroRaw ->
            // Limpiamos y obtenemos el primer término del usuario
            val term = filtroRaw?.split("\\s+".toRegex())
                ?.filter { it.isNotBlank() }
                ?.getOrNull(0) // Tomamos solo el primer término, como en tu código original

            val filter: String = if (term.isNullOrBlank()) {
                "%" // Si el usuario no ha escrito nada, buscamos todo ("%")
            } else if (term.contains("*")) {
                // Si el término contiene '*', solo reemplazamos '*' por '%'
                term.replace("*", "%")
            } else {
                // Si el término NO contiene '*', lo envolvemos con '%' para búsqueda "contiene"
                "%$term%"
            }
            printIncDao.filterByText(_docEntry.value, filter)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    var ultimoEliminado: PIncEntity? = null
    fun deleteItem(inc:PIncEntity){
        viewModelScope.launch {
            ultimoEliminado = inc
            printIncDao.delete(inc)
        }
    }

    fun restaurarUltimoEliminado(){
        viewModelScope.launch {
            ultimoEliminado?.let { printIncDao.insert(it) }
            ultimoEliminado = null
        }
    }

    fun updateConteo(cantidad: Double){
        val direrence= _selectedItem.value?.inWhsQty?.minus(cantidad)
        viewModelScope.launch {
            printIncDao.update(_selectedItem.value!!.copy(difference = direrence, countQty = cantidad))
        }
    }

    fun printEtiqueta(inc: PIncEntity){
        viewModelScope.launch {
            loading.value=true
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

            val etiqueta = pesajeDao.getById( inc.codeBar!!)

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