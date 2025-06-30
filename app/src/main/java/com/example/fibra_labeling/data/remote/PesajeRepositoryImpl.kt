package com.example.fibra_labeling.data.remote

import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.ImobPesaje
import com.example.fibra_labeling.data.model.PrintResponse
import com.example.fibra_labeling.data.network.fibraprint.PrintApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.net.UnknownServiceException

class PesajeRepositoryImpl(private val apiservice: PrintApiService): PesajeRepository {
    override suspend fun getPesaje(codeBar: String): Flow<ImobPesaje> = flow {
        emit(apiservice.getPesaje(codeBar))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else{
            throw it
        }
    }

    override suspend fun printPesaje(codeBarValue: CodeBarRequest): Flow<PrintResponse> =flow{
        emit(apiservice.printPesaje(codeBarValue))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else{
            throw it
        }
    }


}