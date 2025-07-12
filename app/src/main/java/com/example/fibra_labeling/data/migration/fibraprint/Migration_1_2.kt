package com.example.fibra_labeling.data.migration.fibraprint

import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `api_config` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `empresa` TEXT NOT NULL,
                `nombre` TEXT NOT NULL,
                `urlBase` TEXT NOT NULL,
                `isSelect` INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()
        )

        // Insertar el primer valor
        db.execSQL(
            """
            INSERT INTO `api_config` (`empresa`, `nombre`, `urlBase`, `isSelect`) VALUES (
                'FibraPrint',
                'Conexión Print',
                'http://192.168.1.7:8080/backend/',
                1
            )
        """.trimIndent()
        )

    }
}

// 2. Callback para instalaciones nuevas
val roomCallback = object : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL("""
            INSERT INTO `api_config` (`empresa`, `nombre`, `urlBase`, `isSelect`) VALUES (
                'FibraPrint',
                'Conexión Print',
                'http://192.168.1.7:8080/backend/',
                1
            )
        """.trimIndent())

        // inser into

    }
}