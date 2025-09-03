package com.example.fibra_labeling.fibraprint.ui.screen.home_print

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import com.example.fibra_labeling.data.local.dao.fibraprint.ApiConfigDao
import com.example.fibra_labeling.data.remote.fibraprint.PSyncRepository
import com.example.fibra_labeling.datastore.EmpresaPrefs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class HomePrintViewModel(
    private val pSyncRepository: PSyncRepository,
    private val dao: ZplLabelDao,
    private val apiConfigDao: ApiConfigDao,
    private val empresaPrefs: EmpresaPrefs
): ViewModel() {

    private val _hasSelectedConfig = MutableStateFlow<Boolean?>(null)
    val hasSelectedConfig: StateFlow<Boolean?> = _hasSelectedConfig

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    private val _syncMessage = MutableStateFlow("Sincronizando datos...")
    val syncMessage: StateFlow<String> = _syncMessage

    private val _syncError = MutableSharedFlow<String>()
    val syncError = _syncError.asSharedFlow()

    private val _configState = MutableStateFlow(false)
    val configState: StateFlow<Boolean> = _configState

    private var _hasInitialSyncCompleted = MutableStateFlow(false)
    val hasInitialSyncCompleted = _hasInitialSyncCompleted.asStateFlow()

    init {
        // Observa los cambios automáticamente desde el inicio
        observeSelectedConfig()
    }
    val hasInitialSyncCompletedFlow = empresaPrefs.syncCompleted.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )




    fun markInitialSyncCompleted() {
        _hasInitialSyncCompleted.value = true
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSelectedConfig() {
        viewModelScope.launch {
            empresaPrefs.empresaSeleccionada.flatMapLatest { empresa ->
                if (empresa.isNotEmpty()) {
                    Log.e("empresa success", empresa)
                    apiConfigDao.getSelectedConfigByEmpresaFlow(empresa)
                } else {
                    Log.e("empresa fails", "empresa vacia")
                    flowOf(null)
                }
            }.collect { config ->
                Log.e("config result", config.toString())
                val hasConfig = config != null
                _hasSelectedConfig.value = hasConfig

                // Auto-sincronizar cuando hay configuración seleccionada
                // y no estamos ya sincronizando
                if (hasConfig && !_isSyncing.value) {
                    Log.d("HomePrintViewModel", "Configuración encontrada, iniciando sincronización automática")
                    val syncCompte= empresaPrefs.syncCompleted.firstOrNull()
                    if (syncCompte==false){
                        getDataFromApi()
                    }
                }
            }
        }
    }



    // Función para forzar verificación manual
    @OptIn(ExperimentalCoroutinesApi::class)
    fun checkSelectedApiConfig() {
        viewModelScope.launch {
            val empresa = empresaPrefs.empresaSeleccionada.firstOrNull() ?: ""
            Log.e("checkSelectedApiConfig", "Verificando empresa: $empresa")

            if (empresa.isNotEmpty()) {
                try {
                    val config = apiConfigDao.getSelectedConfigByEmpresa(empresa)
                    _hasSelectedConfig.value = config != null
                    Log.e("checkSelectedApiConfig", "Config encontrada: ${config != null}")

                    if (config != null && !_isSyncing.value) {
                        getDataFromApi()
                    }
                } catch (e: Exception) {
                    Log.e("checkSelectedApiConfig", "Error: ${e.message}")
                    _hasSelectedConfig.value = false
                }
            } else {
                _hasSelectedConfig.value = false
            }
        }
    }

    fun vericarConfiguracion() = viewModelScope.launch {
        //recuperamos empresa seleccionada, si es Fibrafil =01 sino 02
        val empresa = empresaPrefs.empresaId.firstOrNull() ?: ""
        if(empresa.isEmpty()){
            return@launch
        }

        dao.getAllLabels(empresa)
            .catch {
                _configState.value = false
            }
            .collect {
                _configState.value = it.isNotEmpty() // Cambiado: true si hay labels
            }
    }

    fun getDataFromApi() {
        // Verificar si hay configuración seleccionada
        if (hasSelectedConfig.value != true) {
            Log.d("HomePrintViewModel", "No hay configuración seleccionada, cancelando sincronización")
            return
        }
        if (hasInitialSyncCompletedFlow.value){
            Log.d("HomePrintViewModel", "Ya hay una sincronización en progreso")
            return
        }

        // Evitar múltiples sincronizaciones simultáneas
        if (_isSyncing.value) {
            Log.d("HomePrintViewModel", "Ya hay una sincronización en progreso")
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

                _syncMessage.value = "Sincronización completada."
                _syncError.emit("success")

                if(!hasInitialSyncCompletedFlow.value){
                    empresaPrefs.setSyncCompleted(true)
                }

            } catch (e: SocketTimeoutException) {
                _syncMessage.value = "Error de conexión: Tiempo de espera agotado"
                _syncError.emit("errorSync")
                Log.e("Sync", "Timeout: ${e.message}")

            } catch (e: Exception) {
                _syncMessage.value = "Error de sincronización: ${e.message}"
                _syncError.emit("errorSync")
                Log.e("Sync", "Error: ${e.message}")

            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun getDataFromApiManual() {
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

                _syncMessage.value = "Enviando datos Pesajes..."
                pSyncRepository.syncEtiquetaDetalle()
                delay(500)

                _syncMessage.value = "Sincronización completada."
                _syncError.emit("success")

            } catch (e: SocketTimeoutException) {
                _syncMessage.value = "Error de conexión: Tiempo de espera agotado"
                _syncError.emit("errorSync")
                Log.e("Sync", "Timeout: ${e.message}")

            } catch (e: Exception) {
                _syncMessage.value = "Error de sincronización: ${e.message}"
                _syncError.emit("errorSync")
                Log.e("Sync", "Error: ${e.message}")

            } finally {
                _isSyncing.value = false
                vericarConfiguracion()
            }
        }
    }

    // Función para refrescar cuando regresas a la pantalla
    fun refreshOnResume() {
        vericarConfiguracion()
        //getDataFromApi() // o como se llame tu método de sync
    }


}