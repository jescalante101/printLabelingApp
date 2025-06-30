package com.example.fibra_labeling.ui.screen.fibra_print.inventario.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PesajeDao
import com.example.fibra_labeling.data.local.entity.fibraprint.POITMEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import com.example.fibra_labeling.datastore.UserLoginPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrintPesajeViewModel(
    private val pesajeDao: PesajeDao,
    private val userLoginPreference: UserLoginPreference
): ViewModel() {

    val _filtro = MutableStateFlow<String>("")
    val filtro= _filtro.asStateFlow()



    @OptIn(ExperimentalCoroutinesApi::class)
    val productos: StateFlow<List<PesajeEntity>> = _filtro
        .flatMapLatest { filtroRaw ->
            // Limpiamos y obtenemos el primer término del usuario
            val term = filtroRaw.split("\\s+".toRegex())
                .filter { it.isNotBlank() }
                .getOrNull(0) // Tomamos solo el primer término, como en tu código original

            val filter: String = if (term.isNullOrBlank()) {
                "%" // Si el usuario no ha escrito nada, buscamos todo ("%")
            } else if (term.contains("*")) {
                // Si el término contiene '*', solo reemplazamos '*' por '%'
                term.replace("*", "%")
            } else {
                // Si el término NO contiene '*', lo envolvemos con '%' para búsqueda "contiene"
                "%$term%"
            }

            pesajeDao.searchPesaje(filter)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())



    fun setFiltro(filtro: String) {
        _filtro.value = filtro
    }

    fun updateUser(){
        viewModelScope.launch {
            userLoginPreference.saveUserLogin("", name = "", docEntry = "")
        }

    }

//    fun findByCodeBar(){
//        viewModelScope.launch {
//            val productos = pesajeDao.getById(filtro.value)
//
//        }
//    }

}