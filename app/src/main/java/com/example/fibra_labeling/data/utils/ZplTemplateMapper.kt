package com.example.fibra_labeling.data.utils

class ZplTemplateMapper {

    companion object {
        /**
         * Mapea los datos a un template ZPL reemplazando las variables
         * @param template El template ZPL con variables como {productName}, {ubicacion}, etc.
         * @param data Los datos a mapear
         * @return El template ZPL con los datos reemplazados
         */
        fun mapDataToTemplate(template: String, data: ZplLabelData): String {
            return template
                .replace("{productName}", data.productName)
                .replace("{ubicacion}", data.ubicacion)
                .replace("{codebar}", data.codebar)
                .replace("^PQ1,0,1,Y", "^PQ${data.copies},0,1,Y")
        }

        /**
         * Versión extendida para el template completo (original)
         */

        fun mapExtendedDataToTemplate(
            template: String,
            productName: String,
            codigo: String,
            ubicacion: String,
            referencia: String,
            maquina: String,
            lote: String,
            codebar: String,
            copies: Int = 1
        ): String {
            return template
                .replace("{productName}", productName)
                .replace("{codigo}", codigo)
                .replace("{ubicacion}", ubicacion)
                .replace("{referencia}", referencia)
                .replace("{maquina}", maquina)
                .replace("{lote}", lote)
                .replace("{codebar}", codebar)
                .replace("^PQ1,0,1,Y", "^PQ${copies},0,1,Y")
        }

        /**
         * Valida que el template contenga todas las variables necesarias
         */
        fun validateTemplate(template: String, requiredVariables: List<String>): Boolean {
            return requiredVariables.all { variable ->
                template.contains("{$variable}")
            }
        }

        /**
         * Obtiene todas las variables encontradas en un template
         */
        fun extractVariables(template: String): List<String> {
            val regex = "\\{([^}]+)\\}".toRegex()
            return regex.findAll(template)
                .map { it.groupValues[1] }
                .distinct()
                .toList()
        }

        /**
         * Mapea cualquier template con cualquier conjunto de datos usando Map
         * @param template Template ZPL con variables como {variable}
         * @param data Map con pares clave-valor para reemplazar
         * @param copies Número de copias a imprimir
         * @return Template ZPL con datos mapeados
         */
        fun mapCustomTemplate(
            template: String,
            data: Map<String, String>,
            copies: Int = 1
        ): String {
            var mappedTemplate = template

            // Reemplazar todas las variables del Map
            data.forEach { (key, value) ->
                mappedTemplate = mappedTemplate.replace("{$key}", value)
            }

            // Reemplazar número de copias
            mappedTemplate = mappedTemplate.replace("^PQ1,0,1,Y", "^PQ${copies},0,1,Y")

            return mappedTemplate
        }

        /**
         * Valida que todos los datos necesarios estén presentes
         */
        fun validateDataForTemplate(template: String, data: Map<String, String>): List<String> {
            val requiredVariables = extractVariables(template)
            return requiredVariables.filter { !data.containsKey(it) }
        }
    }
}