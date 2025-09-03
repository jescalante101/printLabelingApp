package com.example.fibra_labeling.shared.ui.screen.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.datastore.EmpresaPrefs
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val empresaPrefs: EmpresaPrefs
): ViewModel() {

    fun saveEmpresa(empresa: String) {
        viewModelScope.launch {
            empresaPrefs.guardarEmpresa(empresa)
        }
    }

}