package com.example.fibra_labeling.ui.screen.fibra_print.inventario.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintIncDao
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrintIncViewModel(
    private val printIncDao: PrintIncDao
): ViewModel() {
    private val _docEntry = MutableStateFlow<Int>(0)
    private val _search = MutableStateFlow<String?>(null)

    fun setDocEntry(docEntry: Int) {
        _docEntry.value = docEntry
    }

    fun setSearch(query: String?) {
        _search.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val incData: StateFlow<List<PIncEntity>> = _search
        .flatMapLatest { filter ->
            printIncDao.filterByText(_docEntry.value, _search.value ?: "")
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
}