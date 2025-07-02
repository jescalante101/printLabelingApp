package com.example.fibra_labeling.ui.screen.fibrafil.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.remote.fibrafil.SyncRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomeViewModel(
    private val fillRespository: FMaquinaRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

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
                _syncMessage.value = "Recuperando máquinas..."
                fillRespository.syncMaquinas()
                delay(500)

                _syncMessage.value = "Recuperando usuarios..."
                syncRepository.syncUsers()
                delay(500)

                _syncMessage.value = "Recuperando almacenes..."
                syncRepository.syncAlmacen()
                delay(500)

                _syncMessage.value = "Recuperando artículos..."
                syncRepository.syncOitms()
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


    fun getDataFromApiManual() {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Sincronizando datos con el servidor..."
            try {

                _syncMessage.value = "Recuperando máquinas..."
                fillRespository.syncMaquinas()
                delay(500)


                _syncMessage.value = "Recuperando usuarios..."
                syncRepository.syncUsers()
                delay(500)

                _syncMessage.value = "Recuperando almacenes..."
                syncRepository.syncAlmacen()
                delay(500)

                _syncMessage.value = "Recuperando artículos..."
                syncRepository.syncOitms()
                delay(500)


                _syncMessage.value="Terminando sincronización..."
                syncRepository.syncEtiquetaDetalle()
                delay(500)

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

