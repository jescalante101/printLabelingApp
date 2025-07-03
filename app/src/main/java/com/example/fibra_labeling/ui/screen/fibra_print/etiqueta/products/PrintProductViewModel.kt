package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOitmDao
import com.example.fibra_labeling.data.local.entity.fibraprint.POITMEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn


class PrintProductViewModel(
    private val printOitmDao: PrintOitmDao
): ViewModel() {

    val _filtro = MutableStateFlow<String>("")
    val filtro= _filtro.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val productos: StateFlow<List<POITMEntity>> = _filtro
        .flatMapLatest { filtroRaw ->
            val term = filtroRaw.trim().replace("*", "%")
            val pattern = when {
                term.isEmpty() -> "%"         // Usuario no escribe nada: trae todo
                term.startsWith("%") || term.endsWith("%") -> term
                else -> "$term%"
            }
            flow {
                emit(printOitmDao.searchProduct(pattern))
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun setFiltro(filtro: String) {
        _filtro.value = filtro
    }

}