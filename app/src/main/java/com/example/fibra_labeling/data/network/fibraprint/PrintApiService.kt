package com.example.fibra_labeling.data.network.fibraprint

import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.ImobPesaje
import com.example.fibra_labeling.data.model.IsPrintOnlineResponse
import com.example.fibra_labeling.data.model.OITMData
import com.example.fibra_labeling.data.model.OitmResponse
import com.example.fibra_labeling.data.model.PesajeRequest
import com.example.fibra_labeling.data.model.PesajeResponse
import com.example.fibra_labeling.data.model.PrintResponse
import com.example.fibra_labeling.data.model.ProveedorResponse
import com.example.fibra_labeling.data.model.fibrafil.OncInsertResponse
import com.example.fibra_labeling.data.model.fibrafil.users.FilUserResponse
import com.example.fibra_labeling.data.model.fibraprint.OcrdResponse
import com.example.fibra_labeling.data.model.fibraprint.onicbody.OincFormatBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PrintApiService {


    @GET("bycodebar/{codeBar}")
    suspend fun getPesaje(@Path("codeBar") codeBar: String): ImobPesaje

    @POST("print-etiqueta")
    suspend fun printPesaje(@Body codeBarValue: CodeBarRequest): PrintResponse

    @GET("oitm")
    suspend fun getOitms(@Query("filter") filter: String, @Query("page") page: Int, @Query("pageSize") pageSize: Int): OitmResponse

    @GET("proveedor/nombre")
    suspend fun getProveedorName(@Query("code") code: String): ProveedorResponse

    @GET("almacenes")
    suspend fun getAlmacens(): List<AlmacenResponse>

    @POST("insertar-pesaje")
    suspend fun insertPesaje(@Body pesaje: PesajeRequest): PesajeResponse

    @GET("is-print-online")
    suspend fun isPrintOnline(@Query("ip") ip: String, @Query("puerto") puerto: Int): IsPrintOnlineResponse

    @GET("fibraprint/oitms")
    suspend fun getOitms(): List<OITMData>

    @GET("fibraprint/proveedor")
    suspend fun getProveedor(): List<OcrdResponse>

    @GET("fibraprint/users")
    suspend fun getUsers() : List<FilUserResponse>

    @POST("fibraprint/insertar-cabecera-oinc")
    suspend fun sendOincWithDetails(@Body oincRequest: OincFormatBody) : OncInsertResponse
    
    @POST("insertar-pesajes-en-bloque")
    suspend fun sendPesajeEnBloque(@Body pesajes: List<PesajeRequest>): OncInsertResponse



}