package com.example.fibra_labeling.data.remote

import com.example.fibra_labeling.data.model.IsPrintOnlineResponse
import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    suspend fun isPrintOnline(ip: String, puerto: Int): Flow<IsPrintOnlineResponse>
}