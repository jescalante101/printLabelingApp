package com.example.fibra_labeling.data.network

import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.model.PrintResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("pesaje/{codeBar}")
    suspend fun getPesaje(@Path("codeBar") codeBar: String): ImobPasaje


    @POST("pesaje/print")
    suspend fun printPesaje(@Body codeBarValue: CodeBarRequest): PrintResponse

}