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
import com.example.fibra_labeling.data.model.fibrafil.UpdateITWResponse
import com.example.fibra_labeling.data.model.fibrafil.oinc.OincApiResponse
import com.example.fibra_labeling.data.model.fibrafil.oinc.OincInsertApiResponse
import com.example.fibra_labeling.data.model.fibrafil.users.FilUserResponse
import kotlinx.coroutines.flow.Flow

interface FillRepository {
    suspend fun getOitwInfo(string: String): Flow<ProductoDetalleUi>
    suspend fun getOitms(filter: String, page: Int, pageSize: Int): Flow<OitmResponse>
    suspend fun getAlmacens(): Flow<List<AlmacenResponse>>
    suspend fun getMaquinas(filter: String, page: Int, pageSize: Int): Flow<MaquinasResponse>
    suspend fun filPrintEtiqueta(body: FillPrintRequest): Flow<FilPrintResponse>

    suspend fun updateOitwInfo(productoDetalleUi: List<ProductoDetalleUi>): Flow<List<UpdateITWResponse>>
    suspend fun getOincs(): Flow<List<OincApiResponse>>
//    suspend fun insertOinc(oinc: List<OincApiResponse>): Flow<FilPrintResponse>

    suspend fun getStockAlmacen(itemCode: String, whsCode: String): Flow<StockResponse>

    //
    suspend fun getUsers(): Flow<List<FilUserResponse>>
    suspend fun getAllOitms(): Flow<List<OITMData>>
}