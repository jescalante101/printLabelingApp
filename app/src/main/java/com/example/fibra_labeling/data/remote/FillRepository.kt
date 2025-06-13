package com.example.fibra_labeling.data.remote

import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.MaquinasResponse
import com.example.fibra_labeling.data.model.OitmResponse
import com.example.fibra_labeling.data.model.ProductoDetalleUi
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import kotlinx.coroutines.flow.Flow

interface FillRepository {
    suspend fun getOitwInfo(string: String): Flow<ProductoDetalleUi>
    suspend fun getOitms(filter: String, page: Int, pageSize: Int): Flow<OitmResponse>
    suspend fun getAlmacens(): Flow<List<AlmacenResponse>>
    suspend fun getMaquinas(filter: String, page: Int, pageSize: Int): Flow<MaquinasResponse>
    suspend fun filPrintEtiqueta(body:CodeBarRequest): Flow<FilPrintResponse>
}