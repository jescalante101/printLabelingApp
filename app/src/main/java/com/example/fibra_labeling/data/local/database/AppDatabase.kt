package com.example.fibra_labeling.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fibra_labeling.data.local.dao.fibrafil.EtiquetaDetalleDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FMaquinaDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FibIncDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FibOincDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FibOitmDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FilAlmacenDao
import com.example.fibra_labeling.data.local.dao.fibrafil.FilUserDao
import com.example.fibra_labeling.data.local.dao.fibrafil.ZplLabelDao
import com.example.fibra_labeling.data.local.entity.fibrafil.EtiquetaDetalleEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FMaquinaEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibAlmacenEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibIncEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOITMEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FibOincEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.FilUserEntity
import com.example.fibra_labeling.data.local.entity.fibrafil.ZplLabel
import com.example.fibra_labeling.data.migration.MIGRATION_3_4
import com.example.fibra_labeling.data.migration.MIGRATION_4_5
import com.example.fibra_labeling.data.migration.fibrafil.roomCallbackZpl

@Database(
    entities = [EtiquetaDetalleEntity::class,
        FMaquinaEntity::class, FilUserEntity::class,
        FibOincEntity::class,
        FibIncEntity::class,
        FibAlmacenEntity::class,
        FibOITMEntity::class,
        ZplLabel::class
    ],
    version = 5,//TODO: incrementar la version a  5 para la migracion de la base de datos
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase()  {

    abstract fun etiquetaDetalleDao(): EtiquetaDetalleDao
    abstract fun fMaquinaDao(): FMaquinaDao
    abstract fun filUserDao(): FilUserDao
    abstract fun oincDao(): FibOincDao
    abstract fun incDao(): FibIncDao
    abstract fun almacenDao(): FilAlmacenDao
    abstract fun oitmDao(): FibOitmDao

    abstract fun zplLabelDao(): ZplLabelDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fibra_labeling_db"
                )
                    .fallbackToDestructiveMigrationOnDowngrade(true)
                    .addMigrations(MIGRATION_3_4)
                    .addMigrations(MIGRATION_4_5)
                    .addCallback(roomCallbackZpl)
                    .build().also { INSTANCE = it }
            }
    }
}