package com.example.fibra_labeling.data.network

import com.example.fibra_labeling.data.model.ImobPasaje
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/pesaje/bycodebar/{codeBar}")
    suspend fun getPesaje(@Path("codeBar") codeBar: String): ImobPasaje

}