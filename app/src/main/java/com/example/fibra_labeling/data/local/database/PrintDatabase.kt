package com.example.fibra_labeling.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fibra_labeling.data.local.dao.fibrafil.EtiquetaDetalleDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOitmDao
import com.example.fibra_labeling.data.local.entity.PesajeEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POITMEntity

@Database(
    entities = [
        PesajeEntity::class,
        POITMEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PrintDatabase: RoomDatabase() {

    abstract fun pOitmDao(): PrintOitmDao

    companion object {
        @Volatile private var INSTANCE: PrintDatabase? = null

        fun getDatabase(context: Context): PrintDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PrintDatabase::class.java,
                    "fibra_print_db"
                )
                    .fallbackToDestructiveMigrationOnDowngrade(true)
                    .build().also { INSTANCE = it }
            }
    }
}