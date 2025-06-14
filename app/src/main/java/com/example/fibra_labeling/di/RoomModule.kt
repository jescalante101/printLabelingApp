package com.example.fibra_labeling.di

import androidx.room.Room
import com.example.fibra_labeling.data.local.AppDatabase
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(
            get(), // contexto de Android inyectado por Koin
            AppDatabase::class.java,
            "fibra_labeling_db"
        ).build()
    }

    single { get<AppDatabase>().etiquetaDetalleDao() }
    single { get<AppDatabase>().fMaquinaDao() }

}