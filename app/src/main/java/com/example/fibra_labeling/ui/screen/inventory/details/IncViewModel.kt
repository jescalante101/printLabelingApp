package com.example.fibra_labeling.ui.screen.inventory.details


import androidx.lifecycle.ViewModel
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.repository.fibrafil.fibinc.FibIncRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope

class IncViewModel(private val fibIncRepository: FibIncRepository): ViewModel() {

    val incData: StateFlow<List<FibIncEntity>> = fibIncRepository.getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )


}