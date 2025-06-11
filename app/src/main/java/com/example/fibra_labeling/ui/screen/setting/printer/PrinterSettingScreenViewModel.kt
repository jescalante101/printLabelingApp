package com.example.fibra_labeling.ui.screen.setting.printer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.repository.SettingRepository
import com.example.fibra_labeling.datastore.ImpresoraPreferences
import com.example.fibra_labeling.ui.screen.setting.printer.form.PrintFormErrorState
import com.example.fibra_labeling.ui.screen.setting.printer.form.PrintFormState
import com.example.fibra_labeling.ui.screen.setting.printer.form.isFormValid
import com.example.fibra_labeling.ui.screen.setting.printer.form.validatePrintForm
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PrinterSettingScreenViewModel(
    private val impresoraPreferences: ImpresoraPreferences,
    private val settingRepository: SettingRepository
) : ViewModel() {
    var formState by mutableStateOf(PrintFormState())
        private set
    var errorState by mutableStateOf(PrintFormErrorState())
        private set
    var loading by mutableStateOf(false)
        private set
    var resultMsg by mutableStateOf<String?>(null)
        private set

    init {
        // Cargar datos guardados (ejemplo)
        viewModelScope.launch {
            formState = PrintFormState(
                printerName = impresoraPreferences.impresoraName.first(),
                ip = impresoraPreferences.impresoraIp.first(),
                port = impresoraPreferences.impresoraPuerto.first()
            )
            errorState = validatePrintForm(formState)
        }
    }

    fun onPrinterNameChange(value: String) {
        formState = formState.copy(printerName = value)
        validate()
    }

    fun onIpChange(value: String) {
        formState = formState.copy(ip = value)
        validate()
    }

    fun onPortChange(value: String) {
        formState = formState.copy(port = value.filter { it.isDigit() })
        validate()
    }

    private fun validate() {
        errorState = validatePrintForm(formState)
    }

    fun guardar() {
        validate()
        if (isFormValid(errorState)) {
            loading = true
            viewModelScope.launch {
                impresoraPreferences.guardarImpresora(formState.ip, formState.port, formState.printerName)
                loading = false
                resultMsg = "Configuración guardada correctamente."
            }
        } else {
            resultMsg = "Revisa los campos del formulario."
        }
    }

    fun probarConexion() {
        validate()
        if (isFormValid(errorState)) {
            loading = true
            viewModelScope.launch {
                settingRepository.isPrintOnline(formState.ip, formState.port.toInt())
                    .catch {
                        loading = false
                        resultMsg = "Error de conexión."
                    }
                    .collect { result ->
                        loading = false
                        resultMsg = result.message

                        return@collect
                }

            }
        } else {
            resultMsg = "Revisa los campos del formulario."
        }
    }

    fun clearResultMsg() {
        resultMsg = null
    }
}
