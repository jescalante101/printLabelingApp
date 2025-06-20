package com.example.fibra_labeling.data.utils

class ZplLabelPrinter {

    fun printLabel(template: String, data: ZplLabelData): String {
        // Validar template
        val requiredVars = listOf("productName", "ubicacion", "codebar")
        if (!ZplTemplateMapper.validateTemplate(template, requiredVars)) {
            throw IllegalArgumentException("Template no contiene todas las variables requeridas")
        }

        // Mapear datos
        val zplToPrint = ZplTemplateMapper.mapDataToTemplate(template, data)

        // Aquí enviarías a la impresora
        println("Enviando a impresora:")
        println(zplToPrint)

        return zplToPrint
    }
}


////
//// Ejemplo de uso práctico
//fun main() {
//    val template = """
//        ^XA
//        ^CI28
//        ^MMT
//        ^PW400
//        ^LL240
//        ^LS0
//        ^FO5,5^FB390,2,0,C,0
//        ^A0N,18,18
//        ^FD{productName}^FS
//        ^FO5,45^A0N,13,13^FDUbicación:^FS
//        ^FO80,45^A0N,16,16^FD{ubicacion}^FS
//        ^FO5,75^GB390,2,2^FS
//        ^FO40,85^BY2,3,50
//        ^BCN,60,Y,N,N
//        ^FD{codebar}^FS
//        ^PQ1,0,1,Y
//        ^XZ
//    """.trimIndent()
//
//    val labelData = ZplLabelData(
//        productName = "Tornillo Hexagonal M8x50mm Acero Inoxidable",
//        ubicacion = "A-15-B",
//        codebar = "1234567890123",
//        copies = 5
//    )
//
//    val printer = ZplLabelPrinter()
//    val finalZpl = printer.printLabel(template, labelData)
//
//    // Mostrar variables encontradas en el template
//    val variables = ZplTemplateMapper.extractVariables(template)
//    println("Variables encontradas: $variables")
//}