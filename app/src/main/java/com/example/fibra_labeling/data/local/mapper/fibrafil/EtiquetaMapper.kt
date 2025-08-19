package com.example.fibra_labeling.data.local.mapper.fibrafil

import com.example.fibra_labeling.data.local.entity.fibrafil.EtiquetaDetalleEntity
import com.example.fibra_labeling.data.model.fibrafil.ProductoDetalleUi

fun ProductoDetalleUi.toEtiquetaDetalleEntity(
    cantidad: String?, // pásalo explícito porque no está en tu modelo UI
    isSynced: Boolean = false
): EtiquetaDetalleEntity = EtiquetaDetalleEntity(
    itemCode = codigo,
    itemName = productoName,
    u_FIB_Ref1 = lote,
    u_FIB_Ref2 = referencia,
    u_FIB_MachineCode = maquina,
    u_FIB_BinLocation = ubicacion,
    codeBars = codeBar,
    whsCode = whsCode,
    Cantidad = cantidad,
    isSynced = isSynced
)

fun EtiquetaDetalleEntity.toProductoDetalleUi(): ProductoDetalleUi = ProductoDetalleUi(
    codigo = itemCode,
    productoName = itemName,
    lote = u_FIB_Ref1 ?: "",
    referencia = u_FIB_Ref2 ?: "",
    maquina = u_FIB_MachineCode ?: "",
    ubicacion = u_FIB_BinLocation ?: "",
    codeBar = codeBars,
    whsCode = whsCode,
)