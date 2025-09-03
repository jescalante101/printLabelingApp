package com.example.fibra_labeling.shared.ui.screen.setting.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.datastore.GeneralPreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GeneralSettingScreenViewModel(
    private val generalPrefs: GeneralPreference
): ViewModel() {

    val conteoMode: StateFlow<Boolean> =flow {
        generalPrefs.conteoUseMode.catch {
            emit(false)
        }.collect {
            emit(it)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun changeConteoMode(mode: Boolean) {
        viewModelScope.launch {
            generalPrefs.guardarConteoUseMode(mode)
        }
    }



}