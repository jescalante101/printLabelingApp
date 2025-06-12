package com.example.fibra_labeling.data.remote

import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.model.PrintResponse
import kotlinx.coroutines.flow.Flow

interface PesajeRepository {
    suspend fun getPesaje(codeBar: String): Flow<ImobPasaje>
    suspend fun printPesaje(codeBarValue: CodeBarRequest): Flow<PrintResponse>

}