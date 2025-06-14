package com.example.fibra_labeling.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fibra_labeling.data.local.dao.EtiquetaDetalleDao
import com.example.fibra_labeling.data.local.dao.FMaquinaDao
import com.example.fibra_labeling.data.local.dao.FibIncDao
import com.example.fibra_labeling.data.local.dao.FibOincDao
import com.example.fibra_labeling.data.local.dao.FilUserDao
import com.example.fibra_labeling.data.local.entity.fibrafil.EtiquetaDetalleEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FMaquinaEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FilUserEntity

@Database(
    entities = [EtiquetaDetalleEntity::class,
        FMaquinaEntity::class, FilUserEntity::class,
        FibOincEntity::class,
        FibIncEntity::class,
    ], // agrega otras entidades si tienes
    version = 1
)
abstract class AppDatabase: RoomDatabase()  {

    abstract fun etiquetaDetalleDao(): EtiquetaDetalleDao
    abstract fun fMaquinaDao(): FMaquinaDao
    abstract fun filUserDao(): FilUserDao
    abstract fun oincDao(): FibOincDao
    abstract fun incDao(): FibIncDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fibra_labeling_db"
                ).build().also { INSTANCE = it }
            }
    }
}