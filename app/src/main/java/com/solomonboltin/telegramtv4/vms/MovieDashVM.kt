package com.solomonboltin.telegramtv4.vms


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solomonboltin.telegramtv4.tvb.scrappers.interfaces.ScrappedMovie
import com.solomonboltin.telegramtv4.tvb.scrappers.telegram.default.TGMovieScrapper
import com.solomonboltin.telegramtv4.ui.dash.data.DashData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.drinkless.td.libcore.telegram.TdApi
import org.slf4j.LoggerFactory


class MovieDashVM(
    private val clientVm: ClientVM, private val movieScrapper: TGMovieScrapper,
) :
    ViewModel() {
    private val log = LoggerFactory.getLogger(MovieDashVM::class.java)

    // dashData
    private val _dashData = MutableStateFlow(DashData(mutableListOf()))
    val dashData: StateFlow<DashData> = _dashData.asStateFlow()

    suspend fun loadChat(chatScrapper: ChannelScrapper) = coroutineScope {
        chatScrapper.moviesFlow().collect() {
            log.info("ScrappingFlows Movie ${it.info.title}")
            addMovie(it)
            println("ScrappingFlows Movie added ${it.info.title}")
        }
    }

    fun start() {
        val tScrapper = TelegramScrapper(clientVm)
        viewModelScope.launch {
            log.info("ScrappingFlows starting")

//            withContext(Dispatchers.IO) {

                try {
                    val loadChatsQuery = TdApi.LoadChats(null, 800)
                    clientVm.client.send(loadChatsQuery){
                        println("Got results ")
                    }
//                    clientVm.client.send(TdApi.ChatListMain("")
                    tScrapper.chatScrappersFlow().take(2).collect { chatScrapper ->
                        log.info("ScrappingFlows Chat $chatScrapper")
//                        val x = loadChat(chatScrapper)


                        try {
//                            viewModelScope.launch {
                                chatScrapper.moviesFlow().take(5).collect() {
                                    log.info("ScrappingFlows Movie ${it.info.title}")
                                    addMovie(it)
                                    println("ScrappingFlows Movie added ${it.info.title}")
                                }
//                            }
                        } catch (e: Exception) {
                            log.error("ScrappingFlows movie error ${e.message}", )
                            log.error("ScrappingFlows movie error ${e.stackTrace}")
                        }
                    }

                } catch (e: Exception) {
                    log.error("ScrappingFlows chat error", e)
                }
//            }

        }

        log.info("start")
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//
//                movieScrapper.scrapMovies { newMovie ->
//                    addMovie(newMovie)
//                }
//            }
//
//        }
        log.info("start done")
    }

    private fun updateMovieDash(nDash: DashData) {
        log.info("updateMovieDash")
        _dashData.update {
            nDash
        }
    }

    private fun addMovie(movie: ScrappedMovie) {
        val tagName = movie.info.tags.firstOrNull() ?: "כל הסרטים"
        val nDashData = dashData.value.copy().addMovie(tagName, movie)

        log.info("nDashData: $nDashData")
        updateMovieDash(nDashData)

    }

    fun navigateNext() {
        log.info("navigateNext")
        val nDashData = dashData.value.copy().nextMovie()
        println(dashData.value)
        println(nDashData)
        updateMovieDash(nDashData)
    }

    fun navigatePrev() {
        log.info("navigatePrev")
        val nDashData = dashData.value.copy().previousMovie()
        updateMovieDash(nDashData)
    }

    fun navigateNextList() {
        val nDashData = dashData.value.copy().nextList()
        updateMovieDash(nDashData)
    }

    fun navigatePrevList() {
        val nDashData = dashData.value.copy().previousList()
        updateMovieDash(nDashData)
    }
}





