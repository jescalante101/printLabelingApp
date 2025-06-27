package com.example.fibra_labeling.ui.screen.fibra_print.home_print

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.remote.fibraprint.PSyncRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomePrintViewModel(private val pSyncRepository: PSyncRepository): ViewModel() {


    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    private val _syncMessage = MutableStateFlow("Sincronizando datos...")
    val syncMessage: StateFlow<String> = _syncMessage

    private val _syncError = MutableSharedFlow<String>()
    val syncError=_syncError.asSharedFlow()


    init {
        getDataFromApi()
    }


    fun getDataFromApi() {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Sincronizando datos con el servidor..."
            try {


                _syncMessage.value = "Recuperando usuarios..."
                pSyncRepository.syncUsers()
                delay(500)

                _syncMessage.value = "Recuperando almacenes..."
                pSyncRepository.syncAlmacen()
                delay(500)

                _syncMessage.value = "Recuperando artículos..."
                pSyncRepository.syncOitms()
                delay(500)

                _syncMessage.value = "Recuperando Proveedores..."
                pSyncRepository.syncProveedor()
                delay(500)

                // Puedes seguir agregando pasos aquí...

                _syncMessage.value = "Sincronización completada."
                _syncError.emit("success")
                _isSyncing.value=false
            } catch (e: Exception) {
                _syncMessage.value = "Error de sincronización: ${e.message}"
                _syncError.emit("errorSync")
                _isSyncing.value=false
                Log.e("Sync", "No hay conexión: ${e.message}")
            } catch (e: SocketTimeoutException) {
                _syncMessage.value = "Error de sincronización: ${e.message}"
                _syncError.emit("errorSync")
                _isSyncing.value = false
                Log.e("Sync", "No hay conexión: ${e.message}")
            }
        }
    }



}