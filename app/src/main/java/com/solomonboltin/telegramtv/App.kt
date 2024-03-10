package com.solomonboltin.telegramtv

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.util.EventLogger
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
        single { PlayerVM(get(), get()) }
        single { Scratches() }
        single { AppVM() }
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

            val exoPlayer = ExoPlayer.Builder(applicationContext).build().apply {
                addAnalyticsListener(EventLogger(null))
                addListener(object : Player.Listener {
                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        super.onMediaItemTransition(mediaItem, reason)
                        println("EGL_emulation: onMediaItemTransition. mediaItem = ${mediaItem!!.mediaMetadata.toBundle()}, reason = $reason")
                    }

                    override fun onMetadata(metadata: Metadata) {
                        super.onMetadata(metadata)
                        println("EGL_emulation: onMetadata = $metadata")
                    }

                    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                        super.onMediaMetadataChanged(mediaMetadata)
                        Log.i(
                            "EGL_emulation",
                            "onMediaMetadataChanged: mediaMetadata = $mediaMetadata"
                        )
                    }
                })

                playWhenReady = true
                prepare()

            }

            koinModule.single { db }
            koinModule.single { exoPlayer }
            modules(koinModule)
        }
        super.onCreate()

    }
}