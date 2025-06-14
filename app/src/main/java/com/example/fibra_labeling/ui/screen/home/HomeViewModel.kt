package com.example.fibra_labeling.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.remote.SyncRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomeViewModel(private val fillRespository: FMaquinaRepository,private val syncRepository: SyncRepository): ViewModel() {

    init {
        getDataFromApi()
    }

    private fun getDataFromApi(){
        viewModelScope.launch {
            try {
                awaitAll(
                    async {
                        fillRespository.syncMaquinas()
                    },
                    async {
                        syncRepository.syncUsers()
                    }
                )
            }catch (e: Exception){
                // TODO: MOSTAR LOS MENSAJES DE ERROR
                Log.e("Sync", "No hay conexión: ${e.message}")
            }
        }
    }

}