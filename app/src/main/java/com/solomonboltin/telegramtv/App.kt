package com.solomonboltin.telegramtv

import android.app.Application
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
        single { MovieDashVM(get()) }
//        viewModel { MovieMachineVM(get()) }

    }

    override fun onCreate() {

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(koinModule)
        }
        super.onCreate()

    }
}