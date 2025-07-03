package com.example.fibra_labeling.ui.screen.setting.servidor

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.ApiConfigDao
import com.example.fibra_labeling.data.local.entity.fibraprint.ApiConfigEntity
import com.example.fibra_labeling.datastore.EmpresaPrefs
import com.example.fibra_labeling.ui.screen.setting.servidor.form.ServerSettingFormState
import com.example.fibra_labeling.ui.screen.setting.servidor.form.validate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServerSettingViewModel(
    private val apiConfigDao: ApiConfigDao,
    private val empresaPrefs: EmpresaPrefs
): ViewModel() {

    private val _formState = mutableStateOf(ServerSettingFormState())
    val formState: State<ServerSettingFormState> = _formState

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
        val validated = _formState.value.validate()
        _formState.value = validated
        if (validated.isValid) {
            // Guardar datos aquí

            viewModelScope.launch {

                val empresa= empresaPrefs.empresaSeleccionada.firstOrNull() ?:""

                apiConfigDao.insertConfig(
                    ApiConfigEntity(
                        nombre = validated.nombre,
                        urlBase = validated.url,
                        empresa = empresa,
                        isSelect = false
                    )
                )
            }
        }
    }

    fun onCancelar() {
        _formState.value = ServerSettingFormState()
    }


}