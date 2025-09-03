package com.example.fibra_labeling.fibrafil.ui.screen.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincWithDetalles
import com.example.fibra_labeling.data.local.repository.fibrafil.oinc.FibOincRepository
import com.example.fibra_labeling.data.remote.fibrafil.SyncRepository
import com.example.fibra_labeling.datastore.UserLoginPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegisterCabeceraViewModel(
    private val fibrOincRepository: FibOincRepository,
    private val userLoginPreference: UserLoginPreference,
    private val syncRepository: SyncRepository

): ViewModel() {

    val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    // Estado para manejar el mensaje de sincronización (éxito o error)
    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery



    @OptIn(ExperimentalCoroutinesApi::class)
    val oincs: StateFlow<List<FibOincWithDetalles>> = _searchQuery
        .flatMapLatest { query ->
            fibrOincRepository.getOincWithDetalles(query)  // Filtramos la consulta con el valor actual de searchQuery
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )


    fun saveUserLogin(userLogin: String,code: String,docEntry: String){
        viewModelScope.launch {
            userLoginPreference.saveUserLogin(code,userLogin,docEntry)
        }

    }
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }


    fun syncEtiquetaEncabezado(docEntry: Long){
       viewModelScope.launch {
           async {
               _loading.value = true
               try {
                   val response = syncRepository.sycOinc(docEntry)
                   if (response.success==true) {
                       _syncMessage.value = response.message
                       _loading.value = false
                   } else {
                       _syncMessage.value = "Error en la sincronización: ${response.message}"
                       _loading.value = false
                   }
               }catch (e: Exception){
                   _syncMessage.value = "Error al sincronizar: ${e.message}"
                   _loading.value = false
               }

           }

       }
    }


}