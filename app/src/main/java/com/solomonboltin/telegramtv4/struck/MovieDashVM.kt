package com.solomonboltin.telegramtv4.struck

// MovieDashVM named viewModel structure that has func start, addMovie(ScrapedMovie), moviesLists: List<MovieList>

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.solomonboltin.telegramtv4.models.Movie
import com.solomonboltin.telegramtv4.telegram.TDataSource
import com.solomonboltin.telegramtv4.ui.movie.MovieInfoBar
import com.solomonboltin.telegramtv4.ui.movie.MoviesListUI
import com.solomonboltin.telegramtv4.ui.movie.newMediaItem
import com.solomonboltin.telegramtv4.vms.ClientVM
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Message
import org.koin.androidx.compose.koinViewModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory


interface ScrappedMovieInfo {
    val title: String
    val year: String
    val description: String
    val rating: String
    val tags: List<String>
}

interface ScrapedMovieImages {
    fun getPoster1(): ImageBitmap?
    fun getPoster2(): ImageBitmap?

}

interface ScrapedMovieFiles {
    fun getDefaultFile(): MediaItem?
}


interface ScrappedMovie {
    val info: ScrappedMovieInfo
    val images: ScrapedMovieImages
    val files: ScrapedMovieFiles

    fun isValidMovie(): Boolean = false

    fun toMovie(): Movie {
        return Movie(
            title = info.title,
            year = info.year,
            description = info.description,
            rating = info.rating,
            tags = info.tags,
//            poster1 = images.getPoster1(),
            file = files.getDefaultFile()
        )
    }

}

interface MovieScrapperA {
    val log: Logger

    val movies: List<ScrappedMovie>

    fun scrapMovies(max: Int = 500, onNewMovie: (ScrappedMovie) -> Unit)

}

data class MessageDetails(val chatId: Long, val messageId: Long)
class TGScrappedMovie(private val clientVm: ClientVM, docMessage: Message) : ScrappedMovie {
    private val originalMessage = docMessage
    private val messageDetails = MessageDetails(docMessage.chatId, docMessage.id)


    private val infoMessage = getMessageBefore(2)
    private val imagesMessage = getMessageBefore(3)

    private val doo = reportLoads()


    override val info: ScrappedMovieInfo = TGScrappedMovieInfo(infoMessage)

    override val images: ScrapedMovieImages = TGScrappedMovieImages(imagesMessage)

    override val files: ScrapedMovieFiles = TGScrappedMovieFiles(docMessage)

    override fun isValidMovie(): Boolean {
        return true
    }

    fun reportLoads(){
        println("infoMessage: $infoMessage")
        println("imagesMessage: $imagesMessage")
        println("originalMessage: $originalMessage")

        // print messages links
        println("links::")
        println("infoMessageLink: ${getMessageLink(infoMessage!!)}")
        println("imagesMessageLink: ${getMessageLink(imagesMessage!!)}")
        println("originalMessageLink: ${getMessageLink(originalMessage)}")
        println("end links::")

    }
    private fun getMessageBefore(bX: Int = 1): Message? {
        val beforeId = messageDetails.messageId - (1048576 * bX )
        return  getMessage(beforeId)

    }

    private fun getMessageLink(message: Message): String {
        val getLinkFunc = TdApi.GetMessageLink(message.chatId, message.id, 0, false, false)

        val results = clientVm.sendBlocked(getLinkFunc)
        val link = results as TdApi.MessageLink
        return link.link
    }
    private fun getMessageDetails(message: Message): MessageDetails {
        val getLinkFunc = TdApi.GetMessageLink(message.chatId, message.id, 0, false, false)

        val results = clientVm.sendBlocked(getLinkFunc)
        val link = results as TdApi.MessageLink
        println("link: ${link.link}")

        // Separate link from this format to chat_id and message_id
        // https://t.me/c/1000000000000/0000000000000

        val chatId = link.link.removePrefix("https://t.me/c/").substringBefore("/")
        val messageId = link.link.removePrefix("https://t.me/c/").substringAfter("/")

        val beforeId = message.id
        val afterId = messageId
        println("beforeId: $beforeId afterId: $afterId")
        return MessageDetails(chatId.toLong(), messageId.toLong())
    }

    private fun getMessage(messageId: Long): Message? {
        val getMessageFunc = TdApi.GetMessage(originalMessage.chatId, messageId)
        val results = clientVm.sendBlocked(getMessageFunc)
        return results as Message
    }
}

class TGScrappedMovieInfo(private val message: Message?) : ScrappedMovieInfo {
    private val titleRegex = Regex("(?<=\")[^\"]+")
    private val yearRegex = Regex("(?<=שנה - )\\d+")
    private val qualityRegex = Regex("(?<=איכות - )\\w+ \\d+p")
    private val ratingRegex = Regex("(?<=דירוג: IMDb לסרט - )\\d+\\.\\d+")
    private val translationRegex = Regex("(?<=תרגום מובנה\\. )\\w+")
    private val genreRegex = Regex("(?<=ז'אנר - )[\\w, ]+")
    private val summaryRegex = Regex("(?<=תקציר👇\n).+")

    private val textMessage = message?.content as TdApi.MessageText
    val text: String = textMessage.text.text

    override val title = titleRegex.find(text)?.value ?: ""
    override val year = (yearRegex.find(text)?.value?.toIntOrNull() ?: 0).toString()
    override val description = summaryRegex.find(text)?.value ?: ""
    override val rating = (ratingRegex.find(text)?.value?.toDoubleOrNull() ?: 0.0).toString()
    override val tags = genreRegex.find(text)?.value?.split(", ") ?: emptyList()

    val quality = qualityRegex.find(text)?.value ?: ""
    val translation = translationRegex.find(text)?.value == "כן"

    val doo = report()
    val xe=3

    fun report(){
        println("title: $title")
        println("year: $year")
        println("description: $description")
        println("rating: $rating")
        println("tags: $tags")
        println("quality: $quality")
        println("translation: $translation")
    }


}

class TGScrappedMovieImages(private val message: Message?) : ScrapedMovieImages {
    override fun getPoster1(): ImageBitmap {
        TODO("Not yet implemented")
    }

    override fun getPoster2(): ImageBitmap {
        TODO("Not yet implemented")
    }

}

class TGScrappedMovieFiles(private val message: Message?) : ScrapedMovieFiles {
    override fun getDefaultFile(): MediaItem? {
        val file = (message?.content as TdApi.MessageVideo).video
        if (file.mimeType != "video/mp4") return null
        return newMediaItem(TDataSource.UriFactory.create(1, file.video))
    }

}


class TGMovieScrapper(private val clientVm: ClientVM) : MovieScrapperA {
    override val log: Logger = LoggerFactory.getLogger(TGMovieScrapper::class.java)

    override val movies: List<ScrappedMovie> = emptyList()

    override fun scrapMovies(max: Int, onNewMovie: (ScrappedMovie) -> Unit) {
        log.info("scrapMovies")
//        https://t.me/c/1170460328/9004
        val chatId = "-1001494692739"
        val searchQuery = TdApi.SearchChatMessages (
            chatId.toLong(),
            "",
            null,
            0L,
            0,
            99,
//            TdApi.SearchMessagesFilterVideo(),
            null,
            0L
        )

//        val searchQuery = TdApi.GetChatHistory(
//            chatId.toLong(), 0L, 0, 90, false
//        )

//        val searchQuery = TdApi.SearchMessages(
//            null,
//            "דירוג",
//            0,
//            0L,
//            0L,
//            90,
//            null,
//            0,
//            0
//        )

        val results = clientVm.sendBlocked(searchQuery)
        println(results)

        if (results is TdApi.Messages) {
            log.info("hello")
            log.info("scrapMovies results: ${results.messages.size}")

            results.messages.forEach { result ->
                try {
                    val movie = TGScrappedMovie(clientVm, result)
                    println(movie.info)
                    if (addMovie(movie)) onNewMovie(movie)
                } catch (e: Exception) {
                    println("Failed loading tgDoc as movie: ${e.message}")
                }

            }
        }
    }

    private fun isInMovieList(movie: ScrappedMovie): Boolean {
        return movies.any { it == movie }
    }

    private fun addMovie(movie: ScrappedMovie): Boolean {
        if (!isInMovieList(movie) && movie.isValidMovie()) {
            movies + movie
            return true
        }
        return false
    }
}


class MovieDashVM(private val clientVm: ClientVM, private val movieScrapper: TGMovieScrapper) :
    ViewModel() {
    private val log = LoggerFactory.getLogger(MovieDashVM::class.java)

    // dashData
    private val _dashData = MutableStateFlow(DashData(emptyList(), 0))
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

    fun updateMovieDash(nDash: DashData) {
        log.info("updateMovieDash")
        _dashData.update {
            nDash
        }
    }

    private fun addMovie(movie: ScrappedMovie) {
        log.info("addMovie")
        val  mainTag = "All movies"
        val nMovieList = DashData.MovieList(mainTag, listOf(movie))
        // check if mainTag exists
        val nMovieLists = if (dashData.value.movieLists.any { it.title == mainTag }) {
            dashData.value.movieLists.map { movieList ->
                if (movieList.title == mainTag) {
                    movieList.copy(movies = movieList.movies + movie)
                } else {
                    movieList
                }
            }
        } else {
            dashData.value.movieLists + nMovieList
        }
        val nDashData = dashData.value.copy(movieLists = nMovieLists)

        println("nDashData: $nDashData")
        updateMovieDash(nDashData)

    }
}

data class DashData(val movieLists: List<MovieList>, val selectedList: Int = 0) {
    val selectedMovie: ScrappedMovie?
        get() = movieLists.getOrNull(0)?.selectedMovie

    data class MovieList(val title: String, val movies: List<ScrappedMovie>) {
        val selectedMovie: ScrappedMovie?
            get() = movies.getOrNull(0)
    }
}

@Composable
fun MovieDashUI(dashData: DashData) {

    val selectedList = dashData.movieLists.getOrNull(0)
    val selectedMovie = selectedList?.movies?.getOrNull(0)
    Text(text = "Hello Dash")

    Column() {
        if (selectedMovie != null) MovieInfoBar(movie = selectedMovie.toMovie())
        dashData.movieLists.forEachIndexed { index, movieList ->
            MoviesListUI(movieList = movieList, selected = (index == 0))
        }
    }
}

@Composable
fun DashUi() {

}

