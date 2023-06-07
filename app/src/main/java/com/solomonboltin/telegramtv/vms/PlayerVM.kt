package com.solomonboltin.telegramtv.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.solomonboltin.telegramtv.data.models.MovieDa
import com.solomonboltin.telegramtv.media.TelegramVideoSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi.File
import org.slf4j.LoggerFactory

//


enum class PlayingState {
    NONE,
    STARTING,
    BUFFERING,
    PLAYING,
    PAUSING,
    UN_PAUSING,
    STOPPING
}

class PlayerVM(private val filesVM: FilesVM, private val exoPlayer: ExoPlayer) : ViewModel() {


    private val logger = LoggerFactory.getLogger(PlayerVM::class.java)

    private val _playingMovie = MutableStateFlow<MovieDa?>(null)
    val playingMovie = _playingMovie.asStateFlow()


    private val _playingState = MutableStateFlow(PlayingState.NONE)
    val playingState = _playingState.asStateFlow()

    private val _playingFile = MutableStateFlow<File?>(null)
    val playingFile = _playingFile.asStateFlow()

    private fun updatePlayingFile(file: File) {
        _playingFile.value = file
    }


    private val mediaSourceFactory =
        ProgressiveMediaSource.Factory(TelegramVideoSource.Factory(filesVM))
    private val mediaSource get() = mediaSourceFactory.createMediaSource(playingMovie.value!!.file!!)

    private val BUFFURING_PERSENT = 0.1





    init {
        viewModelScope.launch {
            // handling file updates in background
            viewModelScope.launch(Dispatchers.Default) {
                handleFileUpdates()
            }

            // handling playing movie updates
            viewModelScope.launch(Dispatchers.Default) {
                handlePlayingMovieUpdates()
            }


            // handling playing state in background
            viewModelScope.launch(Dispatchers.Default) {
                handlePlayingState()
            }
        }
    }

    private suspend fun handlePlayingMovieUpdates() {
        withContext(Dispatchers.Default) {
            playingMovie.map {
                when (it) {
                    null -> {
                        _playingState.value = PlayingState.STOPPING
                    }

                    else -> {
                        logger.info("playing movie up: ${playingMovie.value}")
                        _playingState.value = PlayingState.STARTING
                    }
                }
            }
        }.collect {}
    }

    private suspend fun handlePlayingState() {
        playingState.map {

            when (it) {
                PlayingState.STARTING -> {
                    logger.info("playing file IS: ${playingMovie.value}")
                    filesVM.seekFileOffset(playingMovie.value!!.fileId)
                    _playingState.value = PlayingState.BUFFERING
                }

                PlayingState.BUFFERING -> {
                    var updatedFile = playingFile.value
                    while (updatedFile == null || updatedFile.local.downloadedSize < updatedFile.size * BUFFURING_PERSENT) {
                        delay(500)
                        logger.info("buffering ${updatedFile?.local?.downloadedSize}")
                        updatedFile = playingFile.value
                    }
                    _playingState.value = PlayingState.PLAYING
                }

                PlayingState.PLAYING -> {
                    logger.info("playing state")

                    withContext(Dispatchers.Main) {
                        val ms = mediaSourceFactory.createMediaSource(playingMovie.value!!.file!!)
                        logger.info("playing ms: $ms")
                        exoPlayer.setMediaSource(mediaSource)
                        exoPlayer.play()
                    }
                }

                PlayingState.PAUSING -> exoPlayer.pause()
                PlayingState.UN_PAUSING -> exoPlayer.play()

                PlayingState.STOPPING -> {
                    release()
                }

                PlayingState.NONE -> release()
            }

        }.collect {}
    }

    private suspend fun handleFileUpdates() {

        filesVM.filesUpdateFlow
            .filter {
                logger.info("loadFileUpdates: ${it.id} ${playingMovie.value?.fileId}")
                it.id == playingMovie.value?.fileId
            }
            .collect {
                logger.info("loadFileUpdates: ${it.id} ${playingMovie.value!!.fileId}")
                _playingFile.value = it
            }
    }

    private suspend fun release() {

        withContext(Dispatchers.Main) {
            exoPlayer.pause()
        }
//        viewModelScope.cancel()

    }


    fun setMovie(movieDa: MovieDa) {
        _playingMovie.value = movieDa
    }

    fun unsetMovie() {
        _playingMovie.value = null
    }


    // legacy
    private fun handleFileUpdates1() {
        viewModelScope.launch {
            coroutineScope {
                logger.info("start loadFileUpdates")
                filesVM.filesUpdateFlow
                    .filter {
                        logger.info("loadFileUpdates: ${it.id} ${playingMovie.value!!.fileId}")
                        it.id == playingMovie.value!!.fileId
                    }
                    .collect() {
                        logger.info("loadFileUpdates: ${it.id} ${playingMovie.value!!.fileId}")
                        updatePlayingFile(it)
                    }
            }
        }
    }

    fun playMovieS(movieDa: MovieDa) {
        logger.info("playMovieStart: 1")
        setMovie(movieDa)
        filesVM.seekFileOffset(movieDa.fileId)
        logger.info("playMovieStart: 2")

        viewModelScope.launch {
            handleFileUpdates1()
            logger.info("playMovieStart: 3")
            try {
                playMovie(movieDa)
            } catch (e: Exception) {
                logger.info("playMovieS: $e")
                logger.error("playMovieS: $e")
            }
        }
    }

    private suspend fun playMovie(movieDa: MovieDa) {
        logger.info("playMovie: 4")
        _playingState.value = PlayingState.STARTING


        var updatedFile = playingFile.value
        logger.info("playMovie: ${updatedFile?.id} ${updatedFile?.local?.downloadedSize} / ${updatedFile?.size}")
        while (updatedFile == null || updatedFile.local.downloadedSize < updatedFile.size * BUFFURING_PERSENT) {
            logger.info("buffering... file: ${updatedFile?.id} ${updatedFile?.local?.downloadedSize} / ${updatedFile?.size}")
            _playingState.value = PlayingState.BUFFERING

            delay(500)
            updatedFile = playingFile.value
        }

        _playingState.value = PlayingState.PLAYING
        exoPlayer.setMediaSource(mediaSource)
    }

}