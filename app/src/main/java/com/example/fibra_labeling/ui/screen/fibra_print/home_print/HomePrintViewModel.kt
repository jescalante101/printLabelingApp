package com.example.fibra_labeling.ui.screen.fibra_print.home_print

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import com.example.fibra_labeling.data.local.dao.fibraprint.ApiConfigDao
import com.example.fibra_labeling.data.local.entity.fibraprint.ApiConfigEntity
import com.example.fibra_labeling.data.remote.fibraprint.PSyncRepository
import com.example.fibra_labeling.datastore.EmpresaPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomePrintViewModel(
    private val pSyncRepository: PSyncRepository,
    private val dao: ZplLabelDao,
    private val apiConfigDao: ApiConfigDao,
    private val empresaPrefs: EmpresaPrefs
): ViewModel() {

    private val _apiCount= MutableStateFlow<Int?>(null)
    val apiCount: StateFlow<Int?> = _apiCount
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    private val _syncMessage = MutableStateFlow("Sincronizando datos...")
    val syncMessage: StateFlow<String> = _syncMessage

    private val _syncError = MutableSharedFlow<String>()
    val syncError=_syncError.asSharedFlow()

    private val _configState = MutableStateFlow(false)
    val configState: StateFlow<Boolean> = _configState




    fun getApiSetting(){
        viewModelScope.launch {
            val empresa = empresaPrefs.empresaSeleccionada.firstOrNull() ?: ""
            apiConfigDao.getConfigsByEmpresa(empresa)
                .catch {
                    _apiCount.value=0
                }
                .collect {
                    print(it.size)
                    _apiCount.value=it.size
                }
        }
    }

    fun vericarConfiguracion()=viewModelScope.launch {
        dao.getAllLabels()
            .catch {
                _configState.value=false
            }
            .collect {
            _configState.value= !it.isNotEmpty()
        }

    }


    fun getDataFromApi() {
        if(apiCount.value==null || apiCount.value==0){
            return
        }
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

                _syncMessage.value = "Recuperando Pesajes..."
                pSyncRepository.syncPesajes()
                delay(500)

                // Puedes seguir agregando pasos aquí...

                _syncMessage.value = "Sincronización completada."
                _syncError.emit("success")
                _isSyncing.value=false
                //vericarConfiguracion()
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
        viewModelScope.launch {77.82
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

                _syncMessage.value = "Enviando datos Pesajes..."
                pSyncRepository.syncEtiquetaDetalle()
                delay(500)

                // Puedes seguir agregando pasos aquí...

                _syncMessage.value = "Sincronización completada."
                _syncError.emit("success")
                _isSyncing.value=false
                vericarConfiguracion()
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