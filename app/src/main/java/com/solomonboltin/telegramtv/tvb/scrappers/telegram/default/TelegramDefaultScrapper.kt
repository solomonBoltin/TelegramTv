package com.solomonboltin.telegramtv.tvb.scrappers.telegram.default

import androidx.compose.ui.graphics.ImageBitmap
import com.solomonboltin.telegramtv.tvb.scrappers.interfaces.*
import com.solomonboltin.telegramtv.vms.ClientVM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.drinkless.td.libcore.telegram.TdApi
import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class MessageDetails(val chatId: Long, val messageId: Long)
class TGScrappedMovie(private val clientVm: ClientVM, docMessage: TdApi.Message) : ScrappedMovie {
    private val originalMessage = docMessage
    private val messageDetails = MessageDetails(docMessage.chatId, docMessage.id)


    private val infoMessage = getMessageBefore(2)
    private val imagesMessage = getMessageBefore(3)

    private val doo = reportLoads()


    override val info: ScrappedMovieInfo = TGScrappedMovieInfo(infoMessage)

    override val images: ScrapedMovieImages = TGScrappedMovieImages(clientVm, imagesMessage)

    override val files: ScrapedMovieFiles = TGScrappedMovieFiles(docMessage)



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
    private fun getMessageBefore(bX: Int = 1): TdApi.Message? {
        val beforeId = messageDetails.messageId - (1048576 * bX )
        return  getMessage(beforeId)

    }

    private fun getMessageLink(message: TdApi.Message): String {
        val getLinkFunc = TdApi.GetMessageLink(message.chatId, message.id, 0, false, false)

        val results = clientVm.sendBlocked(getLinkFunc)
        val link = results as TdApi.MessageLink
        return link.link
    }
    private fun getMessageDetails(message: TdApi.Message): MessageDetails {
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

    private fun getMessage(messageId: Long): TdApi.Message? {
        val getMessageFunc = TdApi.GetMessage(originalMessage.chatId, messageId)
        val results = clientVm.sendBlocked(getMessageFunc)
        return results as TdApi.Message
    }
}

class TGScrappedMovieInfo(private val message: TdApi.Message?) : ScrappedMovieInfo {
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

class TGScrappedMovieImages(private val clientVm: ClientVM, private val message: TdApi.Message?) : ScrapedMovieImages {
    override fun getPoster1(): String? {
        if (message?.content is TdApi.MessagePhoto) {
            val content = message.content as TdApi.MessagePhoto
            val photo = content.photo.sizes[0].photo
            val file = clientVm.sendBlocked(TdApi.DownloadFile(photo.id, 1, 0, 0, true))
            if (file is TdApi.File) {
                return file.local.path
            }
        }
        return null
    }

    override fun getPoster2(): ImageBitmap {
        TODO("Not yet implemented")
    }

}

class TGScrappedMovieFiles(private val message: TdApi.Message?) : ScrapedMovieFiles {
    override fun getDefaultVideo(): TdApi.MessageVideo {
       return (message?.content as TdApi.MessageVideo)
    }



}


class TGMovieScrapper(private val clientVm: ClientVM) : MovieScrapperA {
    override val log: Logger = LoggerFactory.getLogger(TGMovieScrapper::class.java)

    override val movies: List<ScrappedMovie> = emptyList()

    private val chats : Flow<TdApi.Chat> = emptyFlow()

    fun getChats() {
        val searchChatsQuery = TdApi.SearchChats("", 100)
        val getChatsQuery = TdApi.LoadChats(null, 800)
        val chats = clientVm.sendBlocked(getChatsQuery)
        println(chats)
        println("end")
    }

    override fun scrapMovies(max: Int, onNewMovie: (ScrappedMovie) -> Unit) {
        getChats()
        log.info("scrapMovies")
//        https://t.me/c/1170460328/9004
        val comedyChatId = "-1001494692739"
        val comedySearch = TdApi.SearchChatMessages (
            comedyChatId.toLong(),
            "",
            null,
            0L,
            0,
            99,
            TdApi.SearchMessagesFilterVideo(),
            0L
        )

        val actionChatId = "-1001170460328"
        val action = TdApi.SearchChatMessages (
            actionChatId.toLong(),
            "",
            null,
            0L,
            0,
            99,
            TdApi.SearchMessagesFilterVideo(),
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

        val comedyResults = clientVm.sendBlocked(comedySearch)

        if (comedyResults is TdApi.Messages) {
            comedyResults.messages.take(5).forEach { result ->
                try {
                    val movie = TGScrappedMovie(clientVm, result)
                    println(movie.info)
                    if (addMovie(movie)) onNewMovie(movie)
                } catch (e: Exception) {
                    println("Failed loading tgDoc as movie: ${e.message}")
                }

            }
        }

        val actionResults = clientVm.sendBlocked(action)

        if (actionResults is TdApi.Messages) {
            actionResults.messages.take(5).forEach { result ->
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
        if (!isInMovieList(movie) && movie.isValidMovie) {
            movies + movie
            return true
        }
        return false
    }
}
