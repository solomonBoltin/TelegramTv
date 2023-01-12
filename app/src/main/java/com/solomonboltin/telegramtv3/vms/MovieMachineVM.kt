package com.solomonboltin.telegramtv3.vms

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solomonboltin.telegramtv3.models.Movie
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Message
import org.drinkless.td.libcore.telegram.TdApi.Messages
import org.slf4j.LoggerFactory

class MovieMachineVM(private val clientVm: ClientVM) : ViewModel() {
    private val log = LoggerFactory.getLogger(MovieMachineVM::class.java)

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies
    private fun addMovie(movie: Movie) {
        _movies.update { it + movie }
    }


    fun searchMovies(query: String, maxResults: Int = 500, callback: (List<Movie>) -> Unit) {
        log.info("searchMovies: $query")
        // repeat
        val searchQuery = TdApi.SearchMessages(
            null,
            "#TVB $query",
            0,
            0,
            0L,
            maxResults,
            TdApi.SearchMessagesFilterEmpty(),
            0,
            0
        )

        clientVm.client.send(searchQuery) { callback(processSearchResult(it)) }
    }

    fun searchMessages(query: String, maxResults: Int = 50) : Flow<Message>{
        log.info("searchMessages: $query")
        // create flow of messages that serches uusig client and maps the results to the flow
        val searchQuery = TdApi.SearchMessages(
            null,
            "#TVB $query",
            0,
            0,
            0L,
            maxResults,
            TdApi.SearchMessagesFilterEmpty(),
            0,
            0
        )

        return callbackFlow {
            clientVm.client.send(searchQuery) {
                log.info("searchMessages res: $query")
                it as Messages
                it.messages.forEach { message ->
                    trySend(message)
                }
            }
        }


    }


    private fun processSearchResult(result: TdApi.Object): List<Movie> {
        Log.i("MovieMachineVM", "Processing search result")
        return when (result) {
            is TdApi.Messages -> {
                Log.i("MovieMachineVM", "Found ${result.messages.size} matching messages")
                result.messages.mapNotNull { processMessage(it) }
            }
            else -> {
                emptyList()
            }
        }

    }

    private fun processMessage(message: Message): Movie? {
        when (message.content) {
            is TdApi.MessageDocument -> {
                val doc = (message.content as TdApi.MessageDocument).document
                val movie = Movie(doc.fileName, "2000", doc.document, message)
                addMovie(movie)
                return movie
            }
            is TdApi.MessageVideo -> {
                val doc = (message.content as TdApi.MessageVideo).video
                val movie = Movie(doc.fileName, "2000", doc.video, message)
                addMovie(movie)
                return movie
            }
        }
        Log.i("MovieMachineVM", "Failed loading movie from message: ${message.id}")
        return null
    }


}