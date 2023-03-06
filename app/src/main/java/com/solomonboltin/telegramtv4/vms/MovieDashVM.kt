package com.solomonboltin.telegramtv4.vms


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solomonboltin.telegramtv4.tvb.models.Movie
import com.solomonboltin.telegramtv4.tvb.scrappers.interfaces.ScrappedMovie
import com.solomonboltin.telegramtv4.tvb.scrappers.telegram.default.TGMovieScrapper
import com.solomonboltin.telegramtv4.ui.dash.data.DashData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.slf4j.LoggerFactory


class MovieDashVM(private val clientVm: ClientVM, private val movieScrapper: TGMovieScrapper) :
    ViewModel() {
    private val log = LoggerFactory.getLogger(MovieDashVM::class.java)

    // dashData
    private val _dashData = MutableStateFlow(DashData(mutableListOf()))
    val dashData: StateFlow<DashData> = _dashData.asStateFlow()



    fun start() {
        log.info("start")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                movieScrapper.scrapMovies { newMovie ->
                    addMovie(newMovie)
                }
            }

        }
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
}





