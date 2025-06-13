package com.example.fibra_labeling.data.remote

import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.MaquinasResponse
import com.example.fibra_labeling.data.model.OitmResponse
import com.example.fibra_labeling.data.model.ProductoDetalleUi
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import com.example.fibra_labeling.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.net.UnknownServiceException

class FillRepositoryImpl(private val apiService: ApiService): FillRepository {

    override suspend fun getOitwInfo(string: String): Flow<ProductoDetalleUi> = flow {
        emit(apiService.getOitwInfo(string))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun getOitms(
        filter: String,
        page: Int,
        pageSize: Int
    ): Flow<OitmResponse> =flow {
        emit(apiService.getOitmsFill(filter, page, pageSize))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun getAlmacens(): Flow<List<AlmacenResponse>> =flow{
        emit(apiService.getAlmacenesFill())
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun getMaquinas(
        filter: String,
        page: Int,
        pageSize: Int
    ): Flow<MaquinasResponse> =flow {
        emit(apiService.getMaquinas(filter, page, pageSize))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun filPrintEtiqueta(
        body:CodeBarRequest
    ): Flow<FilPrintResponse> =flow {
        emit(apiService.filPrintEtiqueta(body))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }
}