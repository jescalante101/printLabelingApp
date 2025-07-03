package com.example.fibra_labeling.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fibra_labeling.data.local.dao.fibrafil.EtiquetaDetalleDao
import com.example.fibra_labeling.data.local.dao.fibraprint.ApiConfigDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PesajeDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintIncDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOcrdDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOincDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOitmDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOusrDao
import com.example.fibra_labeling.data.local.dao.fibraprint.PrintOwhsDao
import com.example.fibra_labeling.data.local.entity.fibraprint.ApiConfigEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.PesajeEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.PIncEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POITMEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POcrdEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POincEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POusrEntity
import com.example.fibra_labeling.data.local.entity.fibraprint.POwhsEntity

@Database(
    entities = [
        PesajeEntity::class,
        POITMEntity::class,
        POcrdEntity::class,
        POwhsEntity::class,
        POusrEntity::class,
        POincEntity::class,
        PIncEntity::class,
        ApiConfigEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PrintDatabase: RoomDatabase() {

    abstract fun pOitmDao(): PrintOitmDao
    abstract fun pOcrdDao(): PrintOcrdDao
    abstract fun pOwhDao(): PrintOwhsDao
    abstract fun pOusrDao(): PrintOusrDao
    abstract fun pOincDao(): PrintOincDao
    abstract fun pIncDao(): PrintIncDao
    abstract fun pesajeDao(): PesajeDao
    abstract fun apiConfigDao(): ApiConfigDao

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