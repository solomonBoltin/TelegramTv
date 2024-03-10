package com.solomonboltin.telegramtv.vms


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solomonboltin.telegramtv.data.TelegramDataLoader
import com.solomonboltin.telegramtv.data.models.MovieDa
import com.solomonboltin.telegramtv.data.models.MyDatabase
import com.solomonboltin.telegramtv.data.scrappers.interfaces.ChatScrapper
import com.solomonboltin.telegramtv.data.scrappers.interfaces.MovieScrapper
import com.solomonboltin.telegramtv.ui.dash.data.DashData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory


private val log = LoggerFactory.getLogger(MovieDashVM::class.java)






class MovieList(
    val moviesFlow: Flow<MovieScrapper?>,
    val chatId: Long = 0L,
    val chatScrapper: ChatScrapper?,
    val movieDas: MutableList<MovieDa> = mutableListOf(),
//    val movieDa: MovieDa? = null,
) {
    fun copy(
        moviesFlow: Flow<MovieScrapper?> = this.moviesFlow,
        chatId: Long = this.chatId,
        chatScrapper: ChatScrapper? = this.chatScrapper,
        movieDas: MutableList<MovieDa> = this.movieDas.toMutableList(),
    ): MovieList {
        return MovieList(moviesFlow, chatId, chatScrapper, movieDas.toMutableList())
    }

    fun addMovie(movie: MovieDa): MovieList {
        movieDas.add(movie)
        return this.copy()
    }
}



class MovieDashVM(private val clientVm: ClientVM, private val db: MyDatabase) : ViewModel() {

    // const's
    private val requiredMoviesAfterFocused = 30
    private val requiredMovieListsAfterSelected = 10
    private val maxInvalidMoviesBeforeSleep = 50


    // dashData
    private val _dashData = MutableStateFlow(DashData(mutableListOf()))
    val dashData: StateFlow<DashData> = _dashData.asStateFlow()

    private val _focusedMovie = MutableStateFlow<MovieDa?>(null)
    val focusedMovie: StateFlow<MovieDa?> = _focusedMovie.asStateFlow()

    private val _movieListsState = MutableStateFlow<List<Long>>(emptyList())
    val movieListsState: StateFlow<List<Long>> = _movieListsState.asStateFlow()

    val _finalMovieLists = mutableMapOf<Long, MutableStateFlow<MovieList>>()
    fun getMovieListFlow(listId: Long): StateFlow<MovieList>? {
        return _finalMovieLists[listId]?.asStateFlow()
    }



    fun updateMovie(listId: Long, movie: MovieDa) {
        val finalMoviesList = _finalMovieLists.getOrPut(listId) {
            MutableStateFlow(
                MovieList(
                    emptyFlow(),
                    listId,
                    null,
                    mutableListOf()
                )
            )
        }


        val newMovieDasList = mutableListOf<MovieDa>(movie, *finalMoviesList.value.movieDas.toTypedArray())

        val newFinalMoviesList = finalMoviesList.value.copy(movieDas = newMovieDasList)
        _finalMovieLists[listId]?.update { finalMoviesList.value.addMovie(movie) }
        // add to movieListsState if not already there
        log.info("NewFlows updateMovie listId: $listId")
        log.info("NewFlows updateMovie finalMoviesListt: ${_finalMovieLists[listId]?.value?.movieDas?.map { it.title }}")


        _movieListsState.update { list ->
            if (list.contains(listId)) {
                list
            } else {
                list + listId
            }
        }
    }


    private val telegramDataLoader = TelegramDataLoader(clientVm)

    // loading chats scope with new dispatcher
    private val loadingChatsScope = CoroutineScope(Dispatchers.IO)
    private val chatLoaderScopes: MutableMap<Long, CoroutineScope> = mutableMapOf()

    // shortcuts
    private val movieLists get() = dashData.value.movieLists




    fun start() {
        loadData()
    }

    private fun loadData() {
        val movieListsFlow = telegramDataLoader
            .chatScrappersFlow()
            .map { chatScrapper ->
                val moviesFlow = chatScrapper
                    .messagesFlow()
                    .onEach {
                        log.info("NewFlows onEach message in chatId: ${chatScrapper.chatId}")
                    }
                    .map { message ->
                        chatScrapper.scrapMovie(message)
                            .let {
                                if (it?.isValidMovie == true) it else null
                            }
                    }
                MovieList(moviesFlow, chatScrapper.chatId, chatScrapper)
            }

        viewModelScope.launch {
            clientVm.requestLoadingChats()
            loadingChatsScope.launch {
                movieListsFlow
                    .onEach {
                        log.info("NewFlows onEach movieList Loading ${it.chatId}")
                    }
                    .sleepWhile {
                        val focusedListIndex = focusedMovie.value?.message?.chatId?.let { listId ->
                            movieListsState.value.indexOf(listId)
                        } ?: 0
                        movieListsState.value.size >= focusedListIndex + requiredMovieListsAfterSelected
                    }
                    .onEach {
                        delay(700)
                    }
                    .collect { movieList ->
                        log.info("NewFlows Loading chat ${movieList.chatId}")
                        var nullsInARow = 0
                        getLoadingChatScope(movieList.chatId)
                            .launch {
                                movieList
                                    .moviesFlow
                                    .onEach {
                                        log.info("NewFlows onEach movie in chatId: ${movieList.chatId} movie?: ${it?.info?.title}")
                                        log.info(
                                            "NewFlows onEach movie " +
                                                    "chatId: ${movieList.chatId} " +
                                                    "movieListSize: ${
                                                        dashData.value.getMovieList(
                                                            movieList.chatId
                                                        )?.movies?.size
                                                    }"
                                        )
                                        if (it == null) {
                                            nullsInARow++
                                        } else {
                                            nullsInARow = 0
                                        }
                                    }
                                    .sleepWhile {
                                        val focusedMovieIndex = _finalMovieLists[movieList.chatId]?.value?.movieDas?.indexOf(
                                            _focusedMovie.value
                                        ) ?: 0

                                        (nullsInARow > 50 || ((_finalMovieLists[movieList.chatId]?.value?.movieDas?.size
                                            ?: 0) >= focusedMovieIndex + requiredMoviesAfterFocused))

                                    }
                                    .filterNotNull()
                                    .collect { movie ->
                                        log.info("NewFlows Loading movie ${movie.info.title}")
                                        addMovie(movie, movieList.chatId)
                                    }
                            }

                    }
            }
            log.info("ScrappingFlows started")
        }
    }


    private fun getLoadingChatScope(chatId: Long): CoroutineScope {
        return chatLoaderScopes[chatId] ?: run {
            val nScope = CoroutineScope(Dispatchers.IO)
            chatLoaderScopes[chatId] = nScope
            nScope
        }
    }


    private fun addMovie(movie: MovieScrapper, chatId: Long) {
//        val tagName = movie.info.tags.firstOrNull() ?: "כל הסרטים"
//        val nDashData = dashData.value.copy().addMovie(chatId, movie)
//        log.info("nDashData: $nDashData")
//        updateMovieDash(nDashData)
        updateMovie(chatId, movie.toMovieDa())
    }



    fun setFocusedMovie(movieDa: MovieDa) {
        log.info("setFocusedMovie")
        _focusedMovie.value = movieDa
    }
}

fun <T> Flow<T>.sleepWhile(condition: () -> Boolean) = flow {
    collect {
        while (condition()) {
            delay(250)
            if (!condition()) {
                log.info("NewFlows sleepWhile end")
            }
        }
        emit(it)
    }
}