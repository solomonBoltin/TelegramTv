package com.solomonboltin.telegramtv.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import com.solomonboltin.telegramtv.data.models.MovieDa
import kotlinx.coroutines.flow.*
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.File
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch


class FilesVM(private val clientVM: ClientVM) : ViewModel() {
    private val log: Logger = LoggerFactory.getLogger(FilesVM::class.java)

    private val _playingMovieDa = MutableStateFlow<MovieDa?>(null)
    val playingMovie = _playingMovieDa.asStateFlow()

    fun setMovie(movieDa: MovieDa) {
        _playingMovieDa.update { movieDa }
    }
    fun stopPlayingMovie() {
        _playingMovieDa.update { null }
    }

    private val files: MutableMap<Int, File> = mutableMapOf()

    var fileUpdatesLatch : CountDownLatch? = null

    private var isConnected = false
    var filesHandler = { it: File ->
        files[it.id] = it
        if (fileUpdatesLatch != null) {
            fileUpdatesLatch!!.countDown()
        }
    }

    private fun connect() {
        if (isConnected) {
            return
        }

        clientVM.setFileUpdatesHandler {
            filesHandler(it)
        }
        isConnected = true
    }

    fun getFile(
        fileId: Int,
        condition: (File) -> Boolean = { true },
        timeOut: Int = 5000,
    ): File? {
        Log.i("FilesVM", "getFile trying to get file $fileId")

        connect()

        return clientVM.sendBlocked(TdApi.GetFile(fileId)) as File?
        repeat(100) {
            if (files[fileId] != null) {

                return files[fileId]!!
            }

            Thread.sleep(timeOut.toLong() / 100)
        }
        return null
    }


    fun seekFileOffset(fileId: Int, offset: Int = 0) {
        connect()
        val file = clientVM.sendBlocked(TdApi.DownloadFile(fileId, 3, offset, 0, false)) as File?
        log.info("Request to download file: $fileId at offset: $offset, was sent")
        filesHandler(file!!)


    }

    fun cancelDownload(fileId: Int) {
        clientVM.client.send(TdApi.CancelDownloadFile(fileId, false)) {
            log.info("Request to stop download file: $fileId, was sent")

        }
    }
}