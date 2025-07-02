package com.example.fibra_labeling.ui.screen.fibra_print.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOincDao
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POincWithDetails
import com.example.fibra_labeling.data.remote.fibrafil.SyncRepository
import com.example.fibra_labeling.data.remote.fibraprint.PSyncRepository
import com.example.fibra_labeling.datastore.UserLoginPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrintOncViewModel(
    private val oincDao: PrintOincDao,
    private val userLoginPreference: UserLoginPreference,
    private val syncRepository: PSyncRepository,
): ViewModel() {

    init {
        viewModelScope.launch {
            userLoginPreference.saveUserLogin("","","")
        }
    }
    private val _searchUser = MutableStateFlow<String?>("")
    fun onSearch(query: String){
        _searchUser.value = query
    }

    val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage

    @OptIn(ExperimentalCoroutinesApi::class)
    val allUsers: StateFlow<List<POincWithDetails>> = _searchUser
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
            oincDao.buscarPorReferenciaONombre(filtro = filter)
        }
        .stateIn(
    viewModelScope,
            SharingStarted.WhileSubscribed(5000),
    emptyList()
    )

    fun saveUserLogin(userLogin: String,code: String,docEntry: String){
        viewModelScope.launch {
            userLoginPreference.saveUserLogin(code,userLogin,docEntry)
        }

    }

    fun deleteOinc(oinc:POincWithDetails){
        viewModelScope.launch {
            oincDao.eliminar(oinc.header)
            oincDao.deletePOincDetails(oinc.details)
        }
    }

    fun syncData(docEntry: Long){
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = syncRepository.sycOinc(docEntry)
                if (response.success==true) {
                    _syncMessage.value = response.message
                    _loading.value = false
                    delay(150)
                    syncPesaje()
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

    private fun syncPesaje(){
        viewModelScope.launch {
           try {
               syncRepository.syncEtiquetaDetalle()
           }catch (e: Exception){
               _loading.value = false
               _syncMessage.value = "Error al sincronizar: ${e.message}"
           }
        }
    }



}