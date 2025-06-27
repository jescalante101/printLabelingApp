package com.example.fibra_labeling.ui.screen.fibra_print.etiqueta.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOitmDao
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOITMEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POITMEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class PrintProductViewModel(
    private val printOitmDao: PrintOitmDao
): ViewModel() {

    val _filtro = MutableStateFlow<String>("")
    val filtro= _filtro.asStateFlow()

    val _totalResult = MutableStateFlow<Int>(0)
    val totalResult: StateFlow<Int> = _totalResult.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val productos: StateFlow<List<POITMEntity>> = _filtro
        .flatMapLatest { filtroRaw ->
            val terms = filtroRaw.split("\\s+".toRegex()).filter { it.isNotBlank() }
            val term1 = terms.getOrNull(0)?.replace("*", "%")?.let { "%$it%" }
            val term2 = terms.getOrNull(1)?.replace("*", "%")?.let { "%$it%" }
            val term3 = terms.getOrNull(2)?.replace("*", "%")?.let { "%$it%" }
            printOitmDao.searchProduct(term1,term2,term3)
                .catch { e ->
                    _totalResult.value = 0
                }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFiltro(filtro: String) {
        _filtro.value = filtro
    }
}