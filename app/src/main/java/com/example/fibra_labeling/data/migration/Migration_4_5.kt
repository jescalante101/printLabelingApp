package com.example.fibra_labeling.data.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `zpl_labels` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `code_name` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `zpl_file` TEXT NOT NULL,
                        `selected` INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())
        // Inserta el template ZPL con formato correcto (¡ojo al formato!)
        database.execSQL("""
            INSERT INTO `zpl_labels` (`code_name`, `name`, `description`, `zpl_file`, `selected`)
            VALUES (
                'ZPL50X30',
                'ETIQUEA 50X30',
                'Template ZPL inicial con placeholders para producto, ubicación y código de barras.',
                '^XA^CI28^MMT^PW400^LL240^LS0^FO5,5^FB390,2,0,C,0^A0N,18,18^FD{productName}^FS^FO5,45^A0N,13,13^FDUbicación:^FS^FO80,45^A0N,16,16^FD{ubicacion}^FS^FO5,75^GB390,2,2^FS^FO40,85^BY2,3,50^BCN,60,Y,N,N^FD{codebar}^FS^PQ1,0,1,Y^XZ',
                1
            )
        """.trimIndent())

        database.execSQL("""
            INSERT INTO `zpl_labels` (`code_name`, `name`, `description`, `zpl_file`, `selected`)
            VALUES (
                'ZPL100X60',
                'ETIQUETA LARGA',
                'Etiqueta con imagen, varios campos y código de barras. Incluye placeholders.',
                '^XA^CI28^MMT^PW800^LL480^LS0^FO680,60^GFA,397,784,14,:Z64:eJyNkbFqwzAQhu8UCsUF0UHeQ6eiwQ/gSRqyy2C/j8mUxxDuIjx0Dp36KKaTyeBn6EmyE5li0xsOTj8f3x3CSisFvhBegU0/wxCm4pMDNlrHLBcCWN9ZGybZZ4AK0YTJiBzYAG3kxmfeIkGRM0CcZbaz2VGyr3PgdGVE1WDjuZEN08hvUzfNvqbOq5OqyXfoP6yTL0Q7mYXMm1AF3432LHjbD2PhfagUEY0xIvpsSTZ7LaOPXqmF7Jt5ghr1JXvSauHIdDhHH2V1HTl/X9iTTYtPn4zA+u7rrAQXfI/y3KO8L81EMjm24oC4y8XNHPCWPuaeEXd8m6cr/OH+5XuXKQdpRB8P21m5nbm9++TOnv6+JEu53ftg5Vtxbp/b9HnuF+0baks=:107C^FO60,70^FB590,2,0,L,0^A0N,28,28^FD{productName}^FS^FO60,135^A0N,22,22^FDCódigo:^FS^FO200,135^A0N,22,22^FD{codigo}^FS^FO60,165^A0N,22,22^FDUbicación:^FS^FO200,165^A0N,22,22^FD{ubicacion}^FS^FO60,195^A0N,22,22^FDReferencia:^FS^FO200,195^A0N,22,22^FD{referencia}^FS^FO60,225^A0N,22,22^FDMáquina:^FS^FO200,225^A0N,22,22^FD{maquina}^FS^FO60,255^A0N,22,22^FDLote:^FS^FO200,255^A0N,22,22^FD{lote}^FS^FO60,285^GB680,2,2^FS^FO200,305^BY3,3,80^BCN,100,Y,N,N^FD{codebar}^FS^PQ1,0,1,Y^XZ',
                0
            )
        """.trimIndent())
    }
}