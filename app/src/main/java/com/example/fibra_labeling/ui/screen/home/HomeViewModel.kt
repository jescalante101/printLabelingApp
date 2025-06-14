package com.example.fibra_labeling.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fibra_labeling.data.local.repository.fibrafil.maquina.FMaquinaRepository
import com.example.fibra_labeling.data.remote.FillRepository
import com.example.fibra_labeling.data.remote.PesajeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HomeViewModel(private val fillRespository: FMaquinaRepository): ViewModel() {

    init {
        getDataFromApi()
    }


    private fun getDataFromApi(){
        viewModelScope.launch {
            try {
                awaitAll(
                    async { fillRespository.syncMaquinas() }
                )
            }catch (e: Exception){
                // TODO: MOSTAR LOS MENSAJES DE ERROR
            }
        }
    }

}