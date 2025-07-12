package com.example.fibra_labeling.ui.screen.setting.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.datastore.GeneralPreference
import com.example.fibra_labeling.datastore.ThemeManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GeneralSettingScreenViewModel(
    private val empresaPrefs: GeneralPreference
): ViewModel() {

    val conteoMode: StateFlow<Boolean> =flow {
        empresaPrefs.conteoUseMode.catch {
            emit(false)
        }.collect {
            emit(it)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun changeConteoMode(mode: Boolean) {
        viewModelScope.launch {
            empresaPrefs.guardarConteoUseMode(mode)
        }
    }



}