package com.solomonboltin.telegramtv

import android.app.Application
import androidx.room.Room
import com.solomonboltin.telegramtv.data.models.MyDatabase
import com.solomonboltin.telegramtv.vms.*
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module


class App : Application() {


    private val koinModule = module {


        single { ClientVM() }
        single { FilesVM(get()) }
        single { resources }
        single { MovieDashVM(get(), get()) }
//        viewModel { MovieMachineVM(get()) }

    }

    override fun onCreate() {

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)

            val db = Room.databaseBuilder(
                applicationContext,
                MyDatabase::class.java,
                "my-database-name"
            ).build()

            koinModule.single { db }
            modules(koinModule)
        }
        super.onCreate()

    }
}