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
                        `zpl_file` TEXT NOT NULL
                    )
                """.trimIndent())
    }
}