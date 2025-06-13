package com.example.fibra_labeling.data.network

import com.example.fibra_labeling.data.model.AlmacenResponse
import com.example.fibra_labeling.data.model.CodeBarRequest
import com.example.fibra_labeling.data.model.ImobPasaje
import com.example.fibra_labeling.data.model.IsPrintOnlineResponse
import com.example.fibra_labeling.data.model.MaquinasResponse
import com.example.fibra_labeling.data.model.OitmResponse
import com.example.fibra_labeling.data.model.PesajeRequest
import com.example.fibra_labeling.data.model.PesajeResponse
import com.example.fibra_labeling.data.model.PrintResponse
import com.example.fibra_labeling.data.model.ProductoDetalleUi
import com.example.fibra_labeling.data.model.ProveedorResponse
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("bycodebar/{codeBar}")
    suspend fun getPesaje(@Path("codeBar") codeBar: String): ImobPasaje

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

    //FIBRAFIL
    @GET("fibrafil/fib-oitminfo-by-code")
    suspend fun getOitwInfo(@Query("code") string: String): ProductoDetalleUi

    @GET("fibrafil/oitmfills")
    suspend fun getOitmsFill(@Query("filter") filter: String, @Query("page") page: Int, @Query("pageSize") pageSize: Int): OitmResponse

    @GET("fibrafil/almacen")
    suspend fun getAlmacenesFill():List<AlmacenResponse>

    @GET("fibrafil/fib-maquinarias")
    suspend fun getMaquinas(@Query("filter") filter: String, @Query("page") page: Int, @Query("pageSize") pageSize: Int): MaquinasResponse

    @POST("fibrafil/print-etiqueta")
    suspend fun filPrintEtiqueta(@Body codeBarValue: CodeBarRequest): FilPrintResponse

}