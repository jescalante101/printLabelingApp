package com.example.fibra_labeling.data.remote

import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.MaquinasResponse
import com.example.fibra_labeling.data.model.OITMData
import com.example.fibra_labeling.data.model.OitmResponse
import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import com.example.fibra_labeling.data.model.fibrafil.FillPrintRequest
import com.example.fibra_labeling.data.model.fibrafil.StockResponse
import com.example.fibra_labeling.data.model.fibrafil.oinc.OincApiResponse
import com.example.fibra_labeling.data.model.fibrafil.oinc.OincInsertApiResponse
import com.example.fibra_labeling.data.model.fibrafil.users.FilUserResponse
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
        body: FillPrintRequest
    ): Flow<FilPrintResponse> =flow {
        emit(apiService.filPrintEtiqueta(body))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun updateOitwInfo(productoDetalleUi: ProductoDetalleUi): Flow<FilPrintResponse> = flow {
        emit(apiService.updateOitwInfo(productoDetalleUi))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun getOincs(): Flow<List<OincApiResponse>> =flow {
        emit(apiService.getOinc())
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun insertOinc(oinc: OincApiResponse): Flow<OincInsertApiResponse> =flow {
        emit(apiService.insertOinc(oinc))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun getStockAlmacen(
        itemCode: String,
        whsCode: String
    ): Flow<StockResponse> =flow {
        emit(apiService.getStockAlmacen(itemCode, whsCode))
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun getUsers(): Flow<List<FilUserResponse>> = flow{
        emit(apiService.getUsers())
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

    override suspend fun getAllOitms(): Flow<List<OITMData>> = flow {
        emit(apiService.getallOitmsFill())
    }.catch {
        if(it is UnknownServiceException){
            throw Exception("No se permite conexión HTTP. Revisa la configuración de seguridad de red.");
        }else {
            throw it
        }
    }

}