package com.example.fibra_labeling.data.repository

import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.OitmResponse
import com.example.fibra_labeling.data.model.PesajeRequest
import com.example.fibra_labeling.data.model.PesajeResponse
import com.example.fibra_labeling.data.model.ProveedorResponse
import com.example.fibra_labeling.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.net.UnknownServiceException

class OitmRespositoryImpl(private val apiservice: ApiService): OitmRepository {

    override suspend fun getOitms(
        filter: String,
        page: Int,
        pageSize: Int
    ): Flow<OitmResponse> = flow {
        emit(apiservice.getOitms(filter, page, pageSize))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun getProveedorName(code: String): Flow<ProveedorResponse> =flow {
        emit(apiservice.getProveedorName(code))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun getAlmacens(): Flow<List<AlmacenResponse>> =flow{
        emit(apiservice.getAlmacens())
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun insertPesaje(pesaje: PesajeRequest): Flow<PesajeResponse> =flow {
        emit(apiservice.insertPesaje(pesaje))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }
}