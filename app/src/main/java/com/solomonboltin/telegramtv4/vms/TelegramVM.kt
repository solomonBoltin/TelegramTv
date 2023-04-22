package com.solomonboltin.telegramtv4.vms

import com.solomonboltin.telegramtv4.tvb.scrappers.telegram.default.TGScrappedMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


class ChannelScrapper(private val clientVM: ClientVM, private val chat: ChatM) {
    fun isMatch(): Boolean {
        val comedyChatId = -1001494692739L
        val actionChatId = -1001170460328L

        return chat.chat.id in listOf(comedyChatId, actionChatId)
    }

    fun moviesFlow(): Flow<TGScrappedMovie> {

        return chat.messagesFlow().map {
            println("ScrappingFlows Mapping message to movie")
            TGScrappedMovie(clientVM, it)
        }.filter {

            it.isValidMovie
        }
    }
}

class TelegramScrapper(private val clientVM: ClientVM) {
    fun chatScrappersFlow() : Flow<ChannelScrapper> {
        return clientVM.chatsFlow().map { chat ->
            if (ChannelScrapper(clientVM, chat).isMatch()) {
                println("ScrappingFlows filtering passed ${chat.chat.id}")

                ChannelScrapper(clientVM, chat)
            } else {
                println("ScrappingFlows filtering ${chat.chat.id}")
                null
            }
        }.filter { it != null }.map { it!! }
    }
}


//class TelegramVM(val clientVM: ClientVM) : ViewModel() {
//    private val log = LoggerFactory.getLogger(TelegramVM::class.java)
//
//
//    fun loadChats() {
//        log.info("ChatFromFlow starting")
//        viewModelScope.launch {
//            chatsFlow().collect {
//                log.info("ChatFromFlow ${it.id} ${it.title} ${it.type}")
//            }
//        }
//    }
//
//    fun messagesFlow() = chatsFlow().flatMapConcat {
//        getMessages(it.id)
//    }
//
//
//    fun messagesFlow(chatId: Long): Flow<TdApi.MessageVideo> {
//        return flow {
//            var lastMessageId = 0L
//            while (true) {
//                val messages = getMessages(chatId, lastMessageId).toList()
//                messages.forEach {
//                    if (it.content is TdApi.MessageVideo) {
//                        emit(it.content as TdApi.MessageVideo)
//                    }
//                    lastMessageId = it.id
//                }
//                println("sleeping: $lastMessageId")
//                delay(1000)
//            }
//        }
//    }
//
//
//}