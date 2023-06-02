package com.solomonboltin.telegramtv.telegram

import android.media.Image
import com.solomonboltin.telegramtv.vms.ClientVM
import kotlinx.coroutines.flow.*
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Message

interface TelegramInterface{
    val clientVM: ClientVM

    val chatPositionUpdatesFlow: Flow<TdApi.ChatPosition>

    fun chatsFlow(): Flow<TelegramChatInterface>
}


interface TelegramChatInterface {
    val clientVM: ClientVM
    val chat: TdApi.Chat

    fun messagesFlow(): Flow<TdApi.Message>
}


interface MoviesDash {
    val clientVM: ClientVM
    val telegram: TelegramInterface

    fun findChatContentAggregator(chatId: Long): ChatContentAggregatorInterface?
    fun contentAggregators() =
        telegram.chatsFlow().map { chat ->
            findChatContentAggregator(chat.chat.id)
        }.filterNotNull()


    suspend fun loadMovies() {
        contentAggregators().take(100).collect {

        }
    }
}

interface ChatContentAggregatorInterface {
    val clientVM: ClientVM
    val chat: TelegramChatInterface

    fun contentFromMessage(message: Message): ContentInterface?

    fun contentUpdatesFlow () = chat.messagesFlow().map { message ->
        contentFromMessage(message)
    }.filterNotNull()
}

interface ContentInterface {
    val clientVM: ClientVM
    val chat: TelegramChatInterface
    val message: TdApi.Message

    fun contentInfo(): ContentInfo

    fun contentImages(): ContentImages

    fun contentFiles(): ContentFiles
}

interface ContentInfo {
    val contentInterface: ContentInterface
    val contentTitle: String
    val contentDescription: String
}

interface ContentImages {
    val defaultPoster: Image
    val otherImages: List<Image>
}

interface ContentFiles {
    val defaultFile: Image
    val otherFiles: List<Image>
}

