package com.solomonboltin.telegramtv3.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solomonboltin.telegramtv3.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.File
import org.drinkless.td.libcore.telegram.TdApi.UpdateFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class FilesVM(val clientVM: ClientVM) : ViewModel() {
    private val log: Logger = LoggerFactory.getLogger(FilesVM::class.java)

    private val _playingMovie = MutableStateFlow<Movie?>(null)
    val playingMovie = _playingMovie.asStateFlow()

    fun setMovie(movie: Movie) {
        _playingMovie.update { movie }
    }

    private val files: MutableMap<Int, File> = mutableMapOf()

    private val filesFlow = MutableSharedFlow<File>(2, 3)
    fun fileFlow(fileId: Int) = filesFlow.filter { it.id == fileId }

    private var isConnected = false
    var filesHandler = { it: File ->
        Log.i("FilesVM", "fileUpdate: $it")
        files[it.id] = it
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
        connect()
        clientVM.client.send(TdApi.GetFile(fileId), null)
        repeat(5) {
            if (files[fileId] != null) {

                return files[fileId]!!
            }
            Log.i("FilesVM", "getFile trying to get file ${files[fileId]}")

            Thread.sleep(timeOut.toLong() / 5)
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