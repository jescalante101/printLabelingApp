package com.example.fibra_labeling.ui.screen.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import com.example.fibra_labeling.data.local.repository.fibrafil.oinc.FibOincRepository
import com.example.fibra_labeling.datastore.UserLoginPreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegisterCabeceraViewModel(fibrOincRepository: FibOincRepository,private val userLoginPreference: UserLoginPreference): ViewModel() {

    val oincs: StateFlow<List<FibOincEntity>> = fibrOincRepository.getAll()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
    fun saveUserLogin(userLogin: String,code: String){
        viewModelScope.launch {
            userLoginPreference.saveUserLogin(code,userLogin)
        }

    }

}