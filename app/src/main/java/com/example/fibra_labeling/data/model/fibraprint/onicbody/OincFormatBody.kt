package com.example.fibra_labeling.data.model.fibraprint.onicbody


import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincWithDetalles
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POincWithDetails
import com.example.fibra_labeling.data.local.mapper.fibrafil.toApiResponse
import com.example.fibra_labeling.data.local.mapper.formatDateToBackend
import com.example.fibra_labeling.data.model.fibrafil.oinc.OincApiResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OincFormatBody(
    @SerialName("detalle")
    val detalle: List<Detalle>,
    @SerialName("docEntry")
    val docEntry: Int,
    @SerialName("u_CountDate")
    val uCountDate: String,
    @SerialName("u_EndTime")
    val uEndTime: String,
    @SerialName("u_MobilDocId")
    val uMobilDocId: Int,
    @SerialName("u_MobilIdentification")
    val uMobilIdentification: String,
    @SerialName("u_Ref")
    val uRef: String,
    @SerialName("u_Remarks")
    val uRemarks: String,
    @SerialName("u_StartTime")
    val uStartTime: String,
    @SerialName("u_UserCodeCount")
    val uUserCodeCount: String,
    @SerialName("u_UserNameCount")
    val uUserNameCount: String
)

// Función de extensión para mapear desde POincEntity a OincFormatBody
fun POincEntity.toOincFormatBody(
    detalle: List<Detalle>,
    uMobilDocId: Int = 0
): OincFormatBody {
    return OincFormatBody(
        detalle = detalle, // Se proporciona externamente
        docEntry = this.docEntry.toInt(), // Long -> Int
        uCountDate = this.u_CountDate ?: "", // String? -> String
        uEndTime = this.u_EndTime ?: "", // String? -> String
        uMobilDocId = uMobilDocId, // Este campo no existe en Room, se debe proporcionar
        uMobilIdentification = this.u_MobilIdentification ?: "", // String? -> String
        uRef = this.u_Ref ?: "", // String? -> String
        uRemarks = this.u_Remarks ?: "", // String? -> String
        uStartTime = this.u_StartTime ?: "", // String? -> String
        uUserCodeCount = this.u_userCodeCount ?: "", // String? -> String
        uUserNameCount = this.u_UserNameCount ?: "" // String? -> String
    )
}

fun POincWithDetails.toApiResponse(): OincFormatBody =
    OincFormatBody(
        docEntry = header.docEntry.toInt(),
        uCountDate = header.u_CountDate ?: "",
        uEndTime = header.u_EndTime ?: "",
        uRef = header.u_Ref ?: "",
        uRemarks = header.u_Remarks ?: "",
        uStartTime = header.u_StartTime ?: "",
        uUserNameCount = header.u_UserNameCount ?: "",
        uUserCodeCount = header.u_userCodeCount ?: "",
        uMobilIdentification = header.u_MobilIdentification ?: "",
        uMobilDocId = header.docEntry.toInt(),
        detalle = details.map { it.toDetalle() },
    )
