package com.example.fibra_labeling.data.model


import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PesajeRequest(
    @SerialName("almacen")
    val almacen: String?,
    @SerialName("codigo")
    val codigo: String?,
    @SerialName("equi")
    val equi: String?,
    @SerialName("lote")
    val lote: String?,
    @SerialName("mLineal")
    val mLineal: String?,
    @SerialName("motivo")
    val motivo: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("pesoBruto")
    val pesoBruto: String?,
    @SerialName("piso")
    val piso: String?,
    @SerialName("provee")
    val provee: String?,
    @SerialName("stepd")
    val stepd: String?,
    @SerialName("ubicacion")
    val ubicacion: String?,
    @SerialName("usuario")
    val usuario: String?,
    @SerialName("zona")
    val zona: String?,
    @SerialName("codeBar")
    val codeBar: String?
)


fun PesajeEntity.toPesajeRequest(): PesajeRequest {
    return PesajeRequest(
        almacen = this.almacen,
        codigo = this.codigo,
        equi = null, // No existe en PesajeEntity
        lote = this.lote,
        mLineal = this.metroLineal,
        motivo = null, // No existe en PesajeEntity
        name = this.nombre,
        pesoBruto = this.peso.toString(),
        piso = this.piso,
        provee = this.proveedor,
        stepd = this.fecha, // Asumiendo que stepd es la fecha
        ubicacion = this.ubicacion,
        usuario = this.usuario,
        zona = this.u_area, // Asumiendo que zona corresponde a u_area
        codeBar = this.codigoBarra,

    )
}
