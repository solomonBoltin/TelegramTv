package com.solomonboltin.telegramtv3

import android.app.Application
import com.solomonboltin.telegramtv3.vms.ClientVM
import com.solomonboltin.telegramtv3.vms.AppVm
import com.solomonboltin.telegramtv3.vms.FilesVM
import com.solomonboltin.telegramtv3.vms.MovieMachineVM
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module


class App : Application() {
    private val koinModule = module {
        single { AppVm() }
        single {  ClientVM() }
        single { FilesVM(get()) }
        viewModel { MovieMachineVM(get()) }

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