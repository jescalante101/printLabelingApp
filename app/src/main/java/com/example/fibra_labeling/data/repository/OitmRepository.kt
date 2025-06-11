package com.example.fibra_labeling.data.repository

import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.OitmResponse
import com.example.fibra_labeling.data.model.PesajeRequest
import com.example.fibra_labeling.data.model.PesajeResponse
import com.example.fibra_labeling.data.model.ProveedorResponse
import kotlinx.coroutines.flow.Flow

interface OitmRepository {
    suspend fun getOitms(filter: String, page: Int, pageSize: Int): Flow<OitmResponse>
    suspend fun getProveedorName(code: String): Flow<ProveedorResponse>
    suspend fun getAlmacens(): Flow<List<AlmacenResponse>>
    suspend fun insertPesaje(pesaje: PesajeRequest): Flow<PesajeResponse>
}