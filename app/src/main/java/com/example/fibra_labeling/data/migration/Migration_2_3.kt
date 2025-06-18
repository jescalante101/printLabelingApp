package com.example.fibra_labeling.data.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Agregar las columnas nuevas
        database.execSQL("ALTER TABLE fib_oinc ADD COLUMN docNum TEXT")
        database.execSQL("ALTER TABLE fib_oinc ADD COLUMN fechaSync TEXT")
    }
}

//val MIGRATION_4_5 = object : Migration(4, 5) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("ALTER TABLE fib_oinc ADD COLUMN docEntry TEXT")
//    }
//}