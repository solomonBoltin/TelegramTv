package com.solomonboltin.telegramtv.data.scrappers.default

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
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi

class DefaultChatScrapper(private val clientVM: ClientVM, private val chat: ChatM) : ChatScrapper {

    override val chatId: Long = chat.chat.id

    override fun isValidChat(): Boolean {
        val chat = clientVM.sendBlocked(TdApi.GetChat(chat.chat.id)) as TdApi.Chat
//        return chat.title.lowercase().contains("tvb")
        return true

    }

    override fun moviesFlow(): Flow<DefaultMovieScrapper> {
        return chat
            .messagesFlow()
            .filter { it.content is TdApi.MessageVideo }
            .map { DefaultMovieScrapper(clientVM, it, it.chatId) }
            .filter { it.isValidMovie }
    }

    override fun messagesFlow(): Flow<TdApi.Message> {
        return chat.messagesFlow()
    }

    override fun scrapMovie(message: TdApi.Message): MovieScrapper? {
        if (message.content !is TdApi.MessageVideo) return null

        return DefaultMovieScrapper(clientVM, message, message.chatId)
            .let { if (it.isValidMovie) it else null }
    }
}

class DefaultMovieScrapper(private val clientVm: ClientVM, videoMessage: TdApi.Message,
                           override val chatId: Long
) :
    MovieScrapper {

    override val info: MovieInfoScrapper = DefaultMovieInfoScrapper(videoMessage)

    override val images: MovieImagesScrapper =
        DefaultScrappedMovieImagesScrapper(clientVm, videoMessage)

    override val files: MovieFilesScrapper = DefaultScrappedMovieFilesScrapper(videoMessage)


}


class DefaultMovieInfoScrapper(private val message: TdApi.Message?) : MovieInfoScrapper {


    private val videoMessage = message?.content as TdApi.MessageVideo?
    private val video: TdApi.Video? = videoMessage?.video


    override val title: String?
        get() = video?.fileName
    override val year: String?
        get() = null
    override val description: String?
        get() = videoMessage?.caption?.text
    override val rating: String?
        get() = null
    override val tags: List<String>
        get() = listOf("chat:${message?.chatId}")

}

class DefaultScrappedMovieImagesScrapper(
    private val clientVm: ClientVM,
    private val message: TdApi.Message?
) : MovieImagesScrapper {
    override fun getPoster1(): String? {
        if (message?.content is TdApi.MessageVideo) {
            val content = message.content as TdApi.MessageVideo
            val photo = content.video.thumbnail?.file
            if (photo != null) {
                val file = clientVm.sendBlocked(TdApi.DownloadFile(photo.id, 1, 0, 0, true))
                if (file is TdApi.File) {
                    return file.local.path
                }
            }
        }
        return null
    }

    override fun getPoster2(): ImageBitmap? {
        return null
    }

}



class DefaultScrappedMovieFilesScrapper(private val message: TdApi.Message?) : MovieFilesScrapper {
    override fun getDefaultVideo(): TdApi.MessageVideo {
        return (message?.content as TdApi.MessageVideo)
    }
}






