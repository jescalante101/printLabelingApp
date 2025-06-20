package com.example.fibra_labeling.di

import androidx.room.Room
import com.example.fibra_labeling.data.local.AppDatabase
import com.example.fibra_labeling.data.migration.MIGRATION_3_4
import com.example.fibra_labeling.data.migration.MIGRATION_4_5
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(
                get(), // contexto de Android inyectado por Koin
                AppDatabase::class.java,
                "fibra_labeling_db"
            )
            .fallbackToDestructiveMigrationOnDowngrade(true)
            .addMigrations(MIGRATION_3_4)
            .addMigrations(MIGRATION_4_5)
            .build()
    }

    single { get<AppDatabase>().etiquetaDetalleDao() }
    single { get<AppDatabase>().fMaquinaDao() }
    single { get<AppDatabase>().filUserDao() }
    single { get<AppDatabase>().oincDao() }
    single { get<AppDatabase>().incDao() }
    single { get<AppDatabase>().almacenDao() }
    single { get<AppDatabase>().oitmDao() }
    single { get<AppDatabase>().zplLabelDao() }
}