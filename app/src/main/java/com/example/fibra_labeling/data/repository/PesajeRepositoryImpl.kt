package com.example.fibra_labeling.data.repository

import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.net.UnknownServiceException

class PesajeRepositoryImpl(private val apiservice: ApiService): PesajeRespository {
    override suspend fun getPesaje(codeBar: String): Flow<ImobPasaje> = flow {
        emit(apiservice.getPesaje(codeBar))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else{
            throw it
        }
    }
}