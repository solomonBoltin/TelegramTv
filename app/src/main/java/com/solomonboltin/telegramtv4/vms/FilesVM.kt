package com.solomonboltin.telegramtv4.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import com.solomonboltin.telegramtv4.models.Movie
import kotlinx.coroutines.flow.*
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.File
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch


class FilesVM(private val clientVM: ClientVM) : ViewModel() {
    private val log: Logger = LoggerFactory.getLogger(FilesVM::class.java)

    private val _playingMovie = MutableStateFlow<Movie?>(null)
    val playingMovie = _playingMovie.asStateFlow()

    fun setMovie(movie: Movie) {
        _playingMovie.update { movie }
    }
    fun stopPlayingMovie() {
        _playingMovie.update { null }
    }

    private val files: MutableMap<Int, File> = mutableMapOf()

    private val filesFlow = MutableSharedFlow<File>(2, 3)
    fun fileFlow(fileId: Int) = filesFlow.filter { it.id == fileId }
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
        clientVM.client.send(TdApi.GetFile(fileId), null)
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
        clientVM.client.send(TdApi.DownloadFile(fileId, 3, offset, 0, false)) {

            log.info("Request to download file: $fileId at offset: $offset, was sent")
            it as File
            filesHandler(it)
        }
    }

    fun cancelDownload(fileId: Int) {
        clientVM.client.send(TdApi.CancelDownloadFile(fileId, false)) {
            log.info("Request to stop download file: $fileId, was sent")

        }
    }
}