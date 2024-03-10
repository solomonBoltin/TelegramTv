package com.solomonboltin.telegramtv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory



class Scratches : ViewModel() {

    private var loadedMovies : MutableList<Int> = mutableListOf<Int>()
    private val expectedMoviesCount: MutableStateFlow<Int> = MutableStateFlow(3)

    private val log: Logger = LoggerFactory.getLogger(Scratches::class.java)


    init {
        log.info("Scratches starting")
        run()
        viewModelScope.launch {
            delay(5000)
            log.info("delay end")
            loadX(3)
        }
    }





    private fun run() {
        val moviesFlow = flowOf(1,2,3,4,5,6,7,8,9)

        this.viewModelScope.launch {
            log.info(moviesFlow.toString())
            moviesFlow
                .takeWhile {
                    log.info("takeWhile. $it")
                    log.info(expectedMoviesCount.value.toString() + " " + loadedMovies.size)
                    while ( expectedMoviesCount.value < loadedMovies.size) {
                        delay(1000)
                    }
                    true
                }
                .onEach {
                    log.info("collecting $it")
                    loadedMovies += it

                }
                .collect{}

        }
    }


    fun loadX(num: Int) {
        expectedMoviesCount.value += num
    }
}






//enum class States {
//    STARTING,
//    LOADING_MOVIES,
//    val state: MutableStateFlow<States?> = MutableStateFlow(null)
//
//
//}

enum class ChatLoaderState {
    LoadingMovies,
    WaitingForUpdates,

}

