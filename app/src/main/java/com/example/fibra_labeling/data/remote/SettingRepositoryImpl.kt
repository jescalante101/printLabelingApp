package com.example.fibra_labeling.data.remote

import com.example.fibra_labeling.data.model.IsPrintOnlineResponse
import com.example.fibra_labeling.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.net.UnknownServiceException

class SettingRepositoryImpl(private val apiService: ApiService) : SettingRepository {

    override suspend fun isPrintOnline(ip: String,puerto: Int): Flow<IsPrintOnlineResponse> = flow{
        emit(apiService.isPrintOnline(ip,puerto))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else{
            throw it
        }
    }

}