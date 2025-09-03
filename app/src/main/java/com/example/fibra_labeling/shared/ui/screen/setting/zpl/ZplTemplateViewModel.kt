package com.example.fibra_labeling.shared.ui.screen.setting.zpl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel
import com.example.fibra_labeling.datastore.EmpresaPrefs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.* // flatMapLatest is in here
import kotlinx.coroutines.launch

class ZplTemplateViewModel(
    private val dao: ZplLabelDao,
    private val empresaPrefs: EmpresaPrefs
) : ViewModel() {

    val empresa: StateFlow<String> = empresaPrefs.empresaId
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            "01"
        )

    // Lista de templates, observable por Compose
    // Now reacts to changes in the 'empresa' StateFlow
    @OptIn(ExperimentalCoroutinesApi::class)
    val labels: StateFlow<List<ZplLabel>> = empresa.flatMapLatest { currentEmpresaId ->
        dao.getAllLabels(currentEmpresaId)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // --- Operaciones CRUD ---

    fun addLabel(label: ZplLabel) = viewModelScope.launch {
        dao.insertLabel(label)
    }

    fun updateLabel(label: ZplLabel) = viewModelScope.launch {
        dao.updateLabel(label)
    }

    fun deleteLabel(label: ZplLabel) = viewModelScope.launch {
        dao.deleteLabel(label)
    }

    fun setSelectedLabel(label: ZplLabel) = viewModelScope.launch {
        dao.unselectAllLabels()
        dao.setSelectedLabel(label.id)
    }

    suspend fun getLabelById(id: Long): ZplLabel? = dao.getLabelById(id)

    // Para buscar por codeName
    suspend fun getLabelByCodeName(codeName: String): ZplLabel? = dao.getLabelByCodeName(codeName)

    // recuperamos la empresa si es Fibrafil=01 sino 02
}