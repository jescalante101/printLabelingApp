package com.example.fibra_labeling.ui.screen.setting.zpl

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ZplTemplateViewModel(
    private val dao: ZplLabelDao
) : ViewModel() {

    // Lista de templates, observable por Compose
    val labels: StateFlow<List<ZplLabel>> = dao.getAllLabels()
        .stateIn(
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
    fun setSelectedLabel(label: ZplLabel)=viewModelScope.launch {
        dao.unselectAllLabels()
        dao.setSelectedLabel(label.id)
    }

    suspend fun getLabelById(id: Long): ZplLabel? = dao.getLabelById(id)

    // Para buscar por codeName
    suspend fun getLabelByCodeName(codeName: String): ZplLabel? = dao.getLabelByCodeName(codeName)
}
