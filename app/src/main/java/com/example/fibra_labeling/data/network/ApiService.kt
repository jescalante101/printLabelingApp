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
import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi
import com.example.fibra_labeling.data.model.ProveedorResponse
import com.example.fibra_labeling.data.model.fibrafil.FilPrintResponse
import com.example.fibra_labeling.data.model.fibrafil.FillPrintRequest
import com.example.fibra_labeling.data.model.fibrafil.StockResponse
import com.example.fibra_labeling.data.model.fibrafil.oinc.OincApiResponse
import com.example.fibra_labeling.data.model.fibrafil.oinc.OincInsertApiResponse
import com.example.fibra_labeling.data.model.fibrafil.users.FilUserResponse
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
    suspend fun filPrintEtiqueta(@Body codeBarValue: FillPrintRequest): FilPrintResponse

    @POST("fibrafil/update-etiqueta")
    suspend fun updateOitwInfo(@Body productoDetalleUi: ProductoDetalleUi): FilPrintResponse

    @GET("fibrafil/list-oinc")
    suspend fun getOinc(): List<OincApiResponse>

    @POST("fibrafil/insertar-cabecera-oinc")
    suspend fun insertOinc(@Body oinc: OincApiResponse): OincInsertApiResponse

    @GET("fibrafil/get-users")
    suspend fun getUsers() : List<FilUserResponse>

    @GET("fibrafil/stock?")
    suspend fun getStockAlmacen(@Query("itemCode") itemCode: String, @Query("whsCode") whsCode: String): StockResponse


}