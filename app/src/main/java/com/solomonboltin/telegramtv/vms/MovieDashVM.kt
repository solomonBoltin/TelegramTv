package com.solomonboltin.telegramtv.vms


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solomonboltin.telegramtv.data.TelegramDataLoader
import com.solomonboltin.telegramtv.data.models.MovieDao
import com.solomonboltin.telegramtv.data.models.MyDatabase
import com.solomonboltin.telegramtv.data.scrappers.interfaces.MovieScrapper
import com.solomonboltin.telegramtv.ui.dash.data.DashData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory



class MovieDashVM(private val clientVm: ClientVM,private val db: MyDatabase) : ViewModel() {

    private val moviesDao: MovieDao = db.movieDao()

    private val log = LoggerFactory.getLogger(MovieDashVM::class.java)

    // dashData
    private val _dashData = MutableStateFlow(DashData(mutableListOf()))
    val dashData: StateFlow<DashData> = _dashData.asStateFlow()

    private val telegramDataLoader = TelegramDataLoader(clientVm)


    fun start() {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            log.info("ScrappingFlows starting")
            try {
                clientVm.requestLoadingChats()
                telegramDataLoader
                    .chatScrappersFlow()
                    .take(5)
                    .collect { chatScrapper ->
                        log.info("ScrappingFlows Chat $chatScrapper")
                        try {
                            chatScrapper
                                .moviesFlow()
                                .take(5)
                                .collect() {
                                    log.info("ScrappingFlows Movie ${it.info.title}")
                                    addMovie(it)
                                }
//                            }
                        } catch (e: Exception) {
                            log.error("ScrappingFlows movie error ${e.message}")
                            log.error("ScrappingFlows movie error ${e.stackTrace}")
                        }
                    }

            } catch (e: Exception) {
                log.error("ScrappingFlows chat error", e)
            }
        }

    }

    private fun addMovie(movie: MovieScrapper) {
        try {
            // moviesDao is a Room DAO
            // i want to insert a movie to the database
            // from a view model


            // next line write code
            // with context of main thread
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    // Perform your database operation here
                    moviesDao.insert(movie.toMovie())
                    // Update UI or perform other actions with the data
                }
            }

            log.info("addMovieDb ${movie.info.title}")
        } catch (e: Exception) {
            log.error("addMovieDb error", e)
        }
        val tagName = movie.info.tags.firstOrNull() ?: "כל הסרטים"
        val nDashData = dashData.value.copy().addMovie(tagName, movie)

        log.info("nDashData: $nDashData")
        updateMovieDash(nDashData)

    }

    private fun updateMovieDash(nDash: DashData) {
        log.info("updateMovieDash")
        _dashData.update {
            nDash
        }
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





