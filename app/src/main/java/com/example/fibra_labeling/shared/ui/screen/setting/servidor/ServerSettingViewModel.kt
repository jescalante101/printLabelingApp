package com.example.fibra_labeling.shared.ui.screen.setting.servidor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.ApiConfigDao
import com.example.fibra_labeling.data.local.entity.fibraprint.ApiConfigEntity
import com.example.fibra_labeling.datastore.EmpresaPrefs
import com.example.fibra_labeling.shared.ui.screen.setting.servidor.form.ServerSettingFormState
import com.example.fibra_labeling.shared.ui.screen.setting.servidor.form.validate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class ServerSettingViewModel(
    private val apiConfigDao: ApiConfigDao,
    private val empresaPrefs: EmpresaPrefs
): ViewModel() {

    private val _formState = mutableStateOf(ServerSettingFormState())
    val formState: State<ServerSettingFormState> = _formState

    //emit message with shareFlow
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    val empresaSeleccionada = empresaPrefs.empresaSeleccionada.stateIn(
        viewModelScope, SharingStarted.Lazily, ""
    )
    @OptIn(ExperimentalCoroutinesApi::class)
    val configs: StateFlow<List<ApiConfigEntity>> = empresaSeleccionada
        .flatMapLatest { empresa ->
            if (empresa.isNotEmpty()) {
                apiConfigDao.getConfigsByEmpresa(empresa)
                    .catch { emit(emptyList()) }
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())



    fun onSeleccionar(setting: ApiConfigEntity) {
        viewModelScope.launch {
            val empresa= empresaPrefs.empresaSeleccionada.firstOrNull() ?:""
            apiConfigDao.deseleccionarConfigsPorEmpresa(empresa)
            apiConfigDao.updateConfig(setting.copy(isSelect = true))
        }
    }

    fun onNombreChange(value: String) {
        _formState.value = _formState.value.copy(nombre = value).validate()
    }

    fun onUrlChange(value: String) {
        _formState.value = _formState.value.copy(url = value).validate()
    }

    fun onGuardar() {

        viewModelScope.launch(Dispatchers.IO) {
            val validated = _formState.value.validate()
            _formState.value = validated
            if (validated.isValid){

                val isReachable = isReachable(validated.url)
                if (!isReachable) {
                    _message.emit("error_url")
                    return@launch
                }

                val empresa= empresaPrefs.empresaSeleccionada.firstOrNull() ?:""
                apiConfigDao.insertConfig(
                    ApiConfigEntity(
                        nombre = validated.nombre,
                        urlBase = validated.url,
                        empresa = empresa,
                        isSelect = false
                    )
                )
                _message.emit("success_register")
                _formState.value = ServerSettingFormState()
            }else{
                _message.emit("error_form")
            }
        }
    }

    fun onCancelar() {
        _formState.value = ServerSettingFormState()
    }

    fun onEliminar(setting: ApiConfigEntity) {
        viewModelScope.launch {
            apiConfigDao.deleteConfig(setting)
        }
    }

   fun updateConfig(setting: ApiConfigEntity) {
        viewModelScope.launch {
            apiConfigDao.updateConfig(setting)
        }
   }

    fun isReachable(url: String, timeout: Int = 3000): Boolean {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = timeout
            connection.readTimeout = timeout
            connection.connect()
            val code = connection.responseCode
            code in 200..399 // Si responde 2xx o 3xx, es válida/accesible
        } catch (e: Exception) {
            false
        }
    }



}