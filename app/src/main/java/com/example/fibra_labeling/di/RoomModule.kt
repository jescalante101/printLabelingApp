package com.example.fibra_labeling.di

import androidx.room.Room
import com.example.fibra_labeling.data.local.database.AppDatabase
import com.example.fibra_labeling.data.local.database.PrintDatabase
import com.example.fibra_labeling.data.migration.MIGRATION_3_4
import com.example.fibra_labeling.data.migration.MIGRATION_4_5
import com.example.fibra_labeling.data.migration.fibrafil.MIGRATION_5_6
import com.example.fibra_labeling.data.migration.fibrafil.roomCallbackZpl
import com.example.fibra_labeling.data.migration.fibraprint.MIGRATION_1_2
import com.example.fibra_labeling.data.migration.fibraprint.roomCallback
import org.koin.dsl.module
import kotlin.math.sin

val roomModule = module {

    //FIBRAFIL DATABASE
    single {
        Room.databaseBuilder(
                get(), // contexto de Android inyectado por Koin
                AppDatabase::class.java,
                "fibra_labeling_db"
            )
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .addMigrations(MIGRATION_3_4)
            .addMigrations(MIGRATION_4_5)
            .addMigrations(MIGRATION_5_6)
            .addCallback(roomCallbackZpl)
            .build()
    }


    //FIBRA PRINT DATABASE
    single {
        Room.databaseBuilder(
                get(), // contexto de Android inyectado por Koin
            PrintDatabase::class.java,
                "fibra_print_db"
            )
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .addMigrations(MIGRATION_1_2)
            .addCallback(roomCallback)
            .build()

    }

    //FIBRAFIL DAOS
    single { get<AppDatabase>().etiquetaDetalleDao() }
    single { get<AppDatabase>().fMaquinaDao() }
    single { get<AppDatabase>().filUserDao() }
    single { get<AppDatabase>().oincDao() }
    single { get<AppDatabase>().incDao() }
    single { get<AppDatabase>().almacenDao() }
    single { get<AppDatabase>().oitmDao() }
    single { get<AppDatabase>().zplLabelDao() }

    //FIBRA PRINT DAOS
    single { get<PrintDatabase>().pOitmDao() }
    single { get<PrintDatabase>().pOcrdDao() }
    single { get<PrintDatabase>().pOwhDao() }
    single { get<PrintDatabase>().pOusrDao() }
    single { get<PrintDatabase>().pOincDao() }
    single{get<PrintDatabase>().pIncDao()}
    single{get<PrintDatabase>().pesajeDao()}
    single{get<PrintDatabase>().apiConfigDao()}

}