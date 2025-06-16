package com.example.fibra_labeling.di

import androidx.room.Room
import com.example.fibra_labeling.data.local.AppDatabase
import org.koin.dsl.module
import kotlin.math.sin

val roomModule = module {
    single {
        Room.databaseBuilder(
                get(), // contexto de Android inyectado por Koin
                AppDatabase::class.java,
                "fibra_labeling_db"
            ).fallbackToDestructiveMigration(false).build()
    }

    single { get<AppDatabase>().etiquetaDetalleDao() }
    single { get<AppDatabase>().fMaquinaDao() }
    single { get<AppDatabase>().filUserDao() }
    single { get<AppDatabase>().oincDao() }
    single { get<AppDatabase>().incDao() }
    single { get<AppDatabase>().almacenDao() }
    single { get<AppDatabase>().oitmDao() }

}