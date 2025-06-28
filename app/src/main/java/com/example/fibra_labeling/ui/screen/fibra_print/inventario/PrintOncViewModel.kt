package com.example.fibra_labeling.ui.screen.fibra_print.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOincDao
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POincWithDetails
import com.example.fibra_labeling.datastore.UserLoginPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PrintOncViewModel(
    private val oincDao: PrintOincDao,
    private val userLoginPreference: UserLoginPreference
): ViewModel() {
    private val _searchUser = MutableStateFlow<String?>(null)
    fun onSearch(query: String){
        _searchUser.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val allUsers: StateFlow<List<POincWithDetails>> = _searchUser
        .flatMapLatest { query ->
            oincDao.buscarPorReferenciaONombre(filtro = query ?:"")
        }
        .stateIn(
    viewModelScope,
            SharingStarted.WhileSubscribed(5000),
    emptyList()
    )

    fun saveUserLogin(userLogin: String,code: String,docEntry: String){
        viewModelScope.launch {
            userLoginPreference.saveUserLogin(code,userLogin,docEntry)
        }

    }

    fun deleteOinc(oinc:POincWithDetails){
        viewModelScope.launch {
            oincDao.eliminar(oinc.header)
            oincDao.deletePOincDetails(oinc.details)
        }
    }



}