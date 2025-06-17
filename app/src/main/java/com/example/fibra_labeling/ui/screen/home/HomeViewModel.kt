package com.example.fibra_labeling.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.remote.SyncRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val fillRespository: FMaquinaRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    private val _syncMessage = MutableStateFlow("Sincronizando datos...")
    val syncMessage: StateFlow<String> = _syncMessage

    init {
        getDataFromApi()
    }

    fun getDataFromApi() {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Sincronizando datos con el servidor..."
            try {
                awaitAll(
                    //recuperando Datos
                    async {
                        _syncMessage.value = "Recuperando máquinas..."
                        fillRespository.syncMaquinas()
                    },
                    async {
                        _syncMessage.value = "Recuperando usuarios..."
                        syncRepository.syncUsers()
                    },
                    async {
                        _syncMessage.value = "Recuperando artículos..."
                        syncRepository.syncOitms()
                    }
                    // Puedes añadir más bloques async si necesitas
                )
                _syncMessage.value = "Sincronización completada."
            } catch (e: Exception) {
                _syncMessage.value = "Error de sincronización: ${e.message}"
                Log.e("Sync", "No hay conexión: ${e.message}")
            } finally {
                // Da tiempo a mostrar el mensaje final
                kotlinx.coroutines.delay(1000)
                _isSyncing.value = false
            }
        }
    }

    fun getDataFromApiManual() {
        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = "Sincronizando datos con el servidor..."
            try {
                awaitAll(

                    async {
                        _syncMessage.value="Enviando datos al servidor..."
                        syncRepository.syncEtiquetaDetalle()
                    },

                    //recuperando Datos
                    async {
                        _syncMessage.value = "Recuperando máquinas..."
                        fillRespository.syncMaquinas()
                    },
                    async {
                        _syncMessage.value = "Recuperando usuarios..."
                        syncRepository.syncUsers()
                    },
                    async {
                        _syncMessage.value = "Recuperando artículos..."
                        syncRepository.syncOitms()
                    }


                )
                _syncMessage.value = "Sincronización completada."
            } catch (e: Exception) {
                _syncMessage.value = "Error de sincronización: ${e.message}"
                Log.e("Sync", "No hay conexión: ${e.message}")
            } finally {
                // Da tiempo a mostrar el mensaje final
                kotlinx.coroutines.delay(1000)
                _isSyncing.value = false
            }
        }
    }


}

