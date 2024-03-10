package com.solomonboltin.telegramtv.data.scrappers.amsalem

import androidx.compose.ui.graphics.ImageBitmap
import com.solomonboltin.telegramtv.data.scrappers.interfaces.ChatScrapper
import com.solomonboltin.telegramtv.data.scrappers.interfaces.MovieFilesScrapper
import com.solomonboltin.telegramtv.data.scrappers.interfaces.MovieImagesScrapper
import com.solomonboltin.telegramtv.data.scrappers.interfaces.MovieInfoScrapper
import com.solomonboltin.telegramtv.data.scrappers.interfaces.MovieScrapper
import com.solomonboltin.telegramtv.vms.ChatM
import com.solomonboltin.telegramtv.vms.ClientVM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

import org.drinkless.td.libcore.telegram.TdApi


// Amsalem is a guy who uploads movies to his channel in a fixed format (see https://t.me/amsalemfilms)
// This class is a scrapper that loads movies from chats that will contain #amasalem in channel info
// the format is as follows:
// first message: movie poster
// second message: movie info
// third message: movie video
// fourth message: movie file

// the info message will also contain #amasalem-info in the message text
// when processing the messages the scrapper will first check if it contains the #amasalem-info tag
// if it does it will try to scrap rest of info from surrounding messages (1 before and 2 after)
//


class AmsalemChatScrapper(private val clientVM: ClientVM, private val chat: ChatM) : ChatScrapper {

    override val chatId: Long = chat.chat.id

    override fun isValidChat(): Boolean {
        val chat = clientVM.sendBlocked(TdApi.GetChat(chat.chat.id)) as TdApi.Chat
        return chat.title.lowercase().contains("amsalem")
    }

    override fun messagesFlow(): Flow<TdApi.Message> {
        return chat.messagesFlow()
    }

    override fun scrapMovie(message: TdApi.Message): MovieScrapper? {
        return AmsalemMovieScrapper(clientVM, message, message.chatId)
            .let { if (it.isValidMovie) it else null }
    }

    override fun moviesFlow(): Flow<MovieScrapper> {
        return chat.messagesFlow().map {
            println("ScrappingFlows Mapping message to movie")
            scrapMovie(it)
        }.filterNotNull()
    }
}

class AmsalemMovieScrapper(private val clientVM: ClientVM, message: TdApi.Message, override val chatId: Long
) : MovieScrapper {
    private val originalMessage = message

    private val imageMessage = getMessageBefore(3)
    private val infoMessage = getMessageBefore(2)
    private val videoMessage = message
    private val videoFileMessage = getMessageAfter(1)

    private val messageDetails = MessageDetails(message.chatId, message.id)
    override val images: MovieImagesScrapper = AmsalemMoviePosterScrapper(clientVM, imageMessage)
    override val info: MovieInfoScrapper = AmsalemMovieInfoScrapper(infoMessage)
    override val files: MovieFilesScrapper = AmsalemMovieFilesScrapper(videoMessage)


    private fun getMessageBefore(bX: Int = 1): TdApi.Message? {
        val beforeId = messageDetails.messageId - (1048576 * bX)
        return getMessage(beforeId)

    }

    private fun getMessageAfter(aX: Int = 1): TdApi.Message? {
        val afterId = messageDetails.messageId + (1048576 * aX)
        return getMessage(afterId)
    }

    private fun getMessage(messageId: Long): TdApi.Message? {
        val getMessageFunc = TdApi.GetMessage(originalMessage.chatId, messageId)
        val results = clientVM.sendBlocked(getMessageFunc)
        return results as TdApi.Message
    }

    data class MessageDetails(val chatId: Long, val messageId: Long)
}

class AmsalemMovieInfoScrapper(private val infoMessage: TdApi.Message?) : MovieInfoScrapper {
    private val titleRegex = Regex("(?<=\")[^\"]+")
    private val yearRegex = Regex("(?<=שנה - )\\d+")
    private val qualityRegex = Regex("(?<=איכות - )\\w+ \\d+p")
    private val ratingRegex = Regex("(?<=דירוג: IMDb לסרט - )\\d+\\.\\d+")
    private val translationRegex = Regex("(?<=תרגום מובנה\\. )\\w+")
    private val genreRegex = Regex("(?<=ז'אנר - )[\\w, ]+")
    private val summaryRegex = Regex("(?<=תקציר👇\n).+")

    private val textMessage = infoMessage?.content as TdApi.MessageText
    val text: String = textMessage.text.text

    override val title = titleRegex.find(text)?.value ?: ""
    override val year = (yearRegex.find(text)?.value?.toIntOrNull() ?: 0).toString()
    override val description = summaryRegex.find(text)?.value ?: ""
    override val rating = (ratingRegex.find(text)?.value?.toDoubleOrNull() ?: 0.0).toString()
    override val tags = genreRegex.find(text)?.value?.split(", ") ?: emptyList()
}

class AmsalemMoviePosterScrapper(
    private val clientVm: ClientVM,
    private val imageMessage: TdApi.Message?
) :
    MovieImagesScrapper {
    override fun getPoster1(): String? {
        if (imageMessage?.content is TdApi.MessagePhoto) {
            val content = imageMessage.content as TdApi.MessagePhoto
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


class AmsalemMovieFilesScrapper(private val message: TdApi.Message?) : MovieFilesScrapper {
    override fun getDefaultVideo() = (message?.content as TdApi.MessageVideo)

}

