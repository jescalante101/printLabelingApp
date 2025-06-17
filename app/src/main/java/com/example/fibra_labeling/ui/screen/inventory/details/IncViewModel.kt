package com.example.fibra_labeling.ui.screen.inventory.details


import androidx.lifecycle.ViewModel
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.repository.fibrafil.fibinc.FibIncRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class IncViewModel(private val fibIncRepository: FibIncRepository): ViewModel() {

    // Estado para el docEntry actualmente consultado (puede ser un parámetro mutable)
    private val _docEntry = MutableStateFlow<Int>(0)
    private val _search = MutableStateFlow<String?>(null)

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



}