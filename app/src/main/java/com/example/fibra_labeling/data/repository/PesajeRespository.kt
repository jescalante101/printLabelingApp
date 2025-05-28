package com.example.fibra_labeling.data.repository

import com.example.fibra_labeling.data.model.ImobPasaje
import kotlinx.coroutines.flow.Flow

interface PesajeRespository {
    suspend fun getPesaje(codeBar: String): Flow<ImobPasaje>
}