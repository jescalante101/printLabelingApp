package com.example.fibra_labeling.data.local.mapper

import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincWithDetalles
import com.example.fibra_labeling.data.model.fibrafil.oinc.IncApiResponse
import com.example.fibra_labeling.data.model.fibrafil.oinc.OincApiResponse

fun FibIncEntity.toApiResponse(lineId: Int): IncApiResponse =
    IncApiResponse(
        docEntry = this.docEntry,
        lineId=null,
        uCountQty = this.U_CountQty?.toInt(),
        uDifference = this.U_Difference?.toInt(),
        uInWhsQty = this.U_InWhsQty?.toInt(),
        uItemCode = this.U_ItemCode,
        uItemName = this.U_ItemName,
        uWhsCode = this.U_WhsCode
    )

fun FibOincWithDetalles.toApiResponse(): OincApiResponse =
    OincApiResponse(
        docEntry = cabecera.docEntry.toInt(),
        uCountDate = formatDateToBackend(cabecera.u_CountDate),
        uEndTime = cabecera.u_EndTime,
        uRef = cabecera.u_Ref,
        uRemarks = cabecera.u_Remarks,
        uStartTime = cabecera.u_StartTime,
        uUserNameCount = cabecera.u_UserNameCount,
        uUserCodeCount = cabecera.u_userCodeCount,
        detalles = detalles.mapIndexed { idx, d -> d.toApiResponse(idx) }
    )


